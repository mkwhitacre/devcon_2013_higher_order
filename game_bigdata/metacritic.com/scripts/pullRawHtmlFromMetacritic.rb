require 'net/http'
require 'uri'
require 'fileutils'

def open(url)
  Net::HTTP.get(URI.parse(url))
end

%w{ ps3 xbox360 pc wii-u 3ds vita ios wii ds psp }.each do |platform|
  
  ('a'..'z').each do |letter|
    
    dir = "/tmp/metacritic.com_raw/#{platform}/#{letter}"
    
    FileUtils.mkdir_p dir
    
    pageNumber = 0
    
    while true
      puts "Retrieving page for platform : #{platform} / letter : #{letter} / page : #{pageNumber}"
      page_content = open("http://www.metacritic.com/browse/games/title/#{platform}/#{letter}?view=condensed&page=#{pageNumber}").gsub(/\r/, "").gsub(/\n/, "").gsub(/\s+/, " ")
      
      currentIndex = 0
      
      # Verify page has results
      if page_content.include? "No Results Found"
        puts "No results found"
        break
      end
      
      File.open(File.join(dir, "page_#{pageNumber}.html"),"w") do |f|
        f.write(page_content)
      end
      
      pageNumber = pageNumber + 1
      
    end
  end
  
end

puts "Complete!"

