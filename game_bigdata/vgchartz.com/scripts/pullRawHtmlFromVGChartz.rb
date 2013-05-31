require 'net/http'
require 'uri'

def open(url)
  Net::HTTP.get(URI.parse(url))
end

@@VGCHARTZ_TOTAL_GAMES_START_OF_NUMBERS_OF_REGEX = /Numbers 1-1 of (\d+,*)+/
@@VGCHARTZ_TOTAL_GAMES_END_OF_NUMBERS_OF_REGEX = / <\/td>/
@@VGCHARTZ_TOTAL_GAMES = /(\d+,*)+\z/

%w{ ps3 x360 pc wiiu 3ds psv ios wii ds psp }.each do |platform|
  page_content_single_result = open("http://www.vgchartz.com/gamedb/?name=&publisher=&platform=#{platform}&genre=&minSales=0&results=1").gsub(/\r/, "").gsub(/\n/, "").gsub(/\s+/, " ")
  
  startOfNumbersOfIndex = page_content_single_result.index(@@VGCHARTZ_TOTAL_GAMES_START_OF_NUMBERS_OF_REGEX) + "Numbers 1-1 of ".size
  endOfNumbersOfIndex = page_content_single_result.index @@VGCHARTZ_TOTAL_GAMES_END_OF_NUMBERS_OF_REGEX, startOfNumbersOfIndex
  
  total = page_content_single_result[startOfNumbersOfIndex, endOfNumbersOfIndex - startOfNumbersOfIndex].gsub(",", "")
  
  puts "Total #{platform} games : #{total}"
  
  page_content_full_result = open("http://www.vgchartz.com/gamedb/?name=&publisher=&platform=#{platform}&genre=&minSales=0&results=#{total}").gsub(/\r/, "").gsub(/\n/, "").gsub(/\s+/, " ")
  
  File.open("/tmp/vgchartz/#{platform}_raw.html", 'w') { |file| file.write(page_content_full_result) }
  
end