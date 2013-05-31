require 'json'
require 'fileutils'

@@META_CRITIC_START_OF_GAME_INFO = /<div class="basic_stat product_title"> <a href="\/game\//
@@META_CRITIC_GAME_CLOSING_INDEX = /<\/a> <\/div>/
@@META_CRITIC_GAME_PARTIAL_NAME = /">.+\z/

@@META_CRITIC_START_OF_GAME_SCORE = /<span class=\"data metascore /
@@META_CRITIC_END_OF_GAME_SCORE = /<\/span>/
@@META_CRITIC_GAME_SCORE = /\">\d+\z/

@@META_CRITIC_START_OF_USER_GAME_SCORE = /<span class="data textscore/
@@META_CRITIC_END_OF_USER_GAME_SCORE = /<\/span>/
@@META_CRITIC_USER_GAME_SCORE = /\">\d\.\d\z/

@@META_CRITIC_GAME_SCORE_TBD =  /\">tbd\z/

@@DATA_DIRECTORY = "/tmp/metacritic.com_raw"

%w{ 3ds ps3 xbox360 pc wii-u vita ios wii ds psp }.each do |platform|
  
  games = Array.new
  
  ('a'..'z').each do |letter|
    
    pageNumber = 0
    
    while true
      
      htmlFile = File.join(@@DATA_DIRECTORY, platform, letter, "page_#{pageNumber}.html")
      
      if !File.exists? htmlFile
        break
      end
      
      puts "Reading page for platform : #{platform} / letter : #{letter} / page : #{pageNumber}"
      
      page_content = IO.read(htmlFile).gsub(/\r/, "").gsub(/\n/, "").gsub(/\s+/, " ")
      
      pageNumber = pageNumber + 1
      currentIndex = 0
      
      currentSize = games.size
      
      begin
        while currentIndex < page_content.size
          
          # Parse game name
          
          gameInfoStartIndex = page_content.index(@@META_CRITIC_START_OF_GAME_INFO, currentIndex) + "<div class=\"basic_stat product_title\"> ".size
          gameNameTagClosingIndex = page_content.index @@META_CRITIC_GAME_CLOSING_INDEX, gameInfoStartIndex
          
          partialGameName = page_content[gameInfoStartIndex, gameNameTagClosingIndex - gameInfoStartIndex].match(@@META_CRITIC_GAME_PARTIAL_NAME).to_a.first
          gameName =  partialGameName[2, partialGameName.size]
          
          # Parse Metacritic game score
          
          scoreInfoStartIndex =  page_content.index(@@META_CRITIC_START_OF_GAME_SCORE, gameNameTagClosingIndex)
          endOfScoreIndex = page_content.index(@@META_CRITIC_END_OF_GAME_SCORE, scoreInfoStartIndex)
          partialGameScore = page_content[scoreInfoStartIndex, endOfScoreIndex - scoreInfoStartIndex]
          
          gameScoreRegex = partialGameScore.match @@META_CRITIC_GAME_SCORE
          if !gameScoreRegex
            gameScoreRegex = partialGameScore.match @@META_CRITIC_GAME_SCORE_TBD
          end
          
          gameScoreRegexString = gameScoreRegex.to_a.first
          gameScore = gameScoreRegexString[2, gameScoreRegexString.size]
          
          # Parse User game score
          
          userScoreInfoStartIndex =  page_content.index(@@META_CRITIC_START_OF_USER_GAME_SCORE, endOfScoreIndex)
          endOfUserScoreIndex = page_content.index(@@META_CRITIC_END_OF_USER_GAME_SCORE, userScoreInfoStartIndex)
          partialUserGameScore = page_content[userScoreInfoStartIndex, endOfUserScoreIndex - userScoreInfoStartIndex]
          
          userGameScoreRegex = partialUserGameScore.match @@META_CRITIC_USER_GAME_SCORE
          if !userGameScoreRegex
            userGameScoreRegex = partialUserGameScore.match @@META_CRITIC_GAME_SCORE_TBD
          end
          
          userGameScoreRegexString = userGameScoreRegex.to_a.first
          userGameScore = userGameScoreRegexString[2, userGameScoreRegexString.size]
          
          # Generate json
          
          games.push({"gameName" => gameName, "metacriticScore" => gameScore, "userScore" => userGameScore})
          
          currentIndex = endOfScoreIndex
        end
      rescue
        # An exception was thrown meaning our regex failed -> no more games to find
      end
      
      puts "Found #{games.size - currentSize} games"
    end
  end
  
  puts "Writing json file"
  
  FileUtils.mkdir_p "/tmp/metacritic.com_processed"

  File.open("/tmp/metacritic.com_processed/#{platform}-games.json","w") do |f|
    f.write(games.to_json)
  end
  
end

puts "Complete!"

