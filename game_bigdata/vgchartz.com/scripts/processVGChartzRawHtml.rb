#!/usr/bin/env ruby
#encoding: UTF-8

require 'json'
require 'fileutils'

@@DATA_DIRECTORY = "data/vgchartz.com_raw"
@@DATA_DIRECTORY_PROCESSED = "data/vgchartz.com_processed"

%w{ 3ds ds ios pc ps3 psp psv wii wiiu x360}.each do |platform|
  
  htmlFile = File.join(@@DATA_DIRECTORY, "#{platform}_raw.html")
      
  if !File.exists? htmlFile
    break
  end
      
  puts "Reading page for platform : #{platform} "+ htmlFile
      
  page_content = IO.read(htmlFile).encode('utf-8', 'binary', :invalid => :replace, :undef => :replace).gsub(/<tr>/, "\n<tr>").gsub(/<\/tr>/, "</tr>\n")
   
  # puts page_content
  FileUtils.mkdir_p @@DATA_DIRECTORY_PROCESSED

  File.open(@@DATA_DIRECTORY_PROCESSED+"/#{platform}-games.html","w") do |f|
   f.write(page_content)
 end
end

puts "Complete!"