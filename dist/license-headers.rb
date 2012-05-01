#!/usr/bin/env ruby

exts = ['html', 'xhtml', 'xml', 'properties', 'java']

# check license in files, add it if not present
require 'find'

updated = 0
conform = 0
dir = File.dirname(__FILE__)
exts.each() do |ext|
 puts "--- processing #{ext} files"
 license = IO.readlines(dir + '/license.'+ext).join()
 Find.find(dir + '/../') do |f|
  if File.file?(f) && File.extname(f) == '.'+ext
   content = ''
   header = ''
   IO.readlines(f).each() do |line|
    if content.length == 0 && header.length == 0 && (ext == 'xml' || ext == 'html' || ext == 'xhtml') && (line.index('<?') == 0 || (line.index('<!') == 0 && line.index('<!--') != 0))
     header += line
    else
     content += line
    end
   end
   if content.index(license) != 0
    File.open(f, 'w') do |f2|
     f2.puts header + license + content
    end
    puts "#{f} updated"
    updated += 1
   else
    conform += 1
   end
  end
 end
end
puts "------------------------------------------------"
puts "#{updated} files updated, #{conform} already compliant"
