#!/usr/bin/env ruby

require 'rubygems'
require 'redcarpet'
require 'nokogiri'
require 'fileutils'
require 'pygments.rb'

# create a custom renderer that allows highlighting of code blocks
class HTMLWithPygmentsAndPants < Redcarpet::Render::HTML
  include Redcarpet::Render::SmartyPants
  def block_code(code, language)
    Pygments.highlight(code, :lexer => language, :options => {:encoding => 'utf-8'})
  end
end

def find(p, tag)
  if p.text
    r = p.text[/^(#{tag}: )(.+)$/, 2]
    if r
      p['id'] = 'metadata'
      return r 
    end
  end
end

def find_split(p, tag)
  s = find(p, tag)
  if s
    return s.split(',').sort 
  end
end

def metadata(source_path, html)
  # TODO canonicalise path
  toc_file='dist/target/toc.html'
  # Markdown doesn't have an metadata syntax, so all we can do is pray ;-)
  # Look for a paragraph that contains tags, which we define by convention
  page_content = Nokogiri::HTML(html)
  technologies = []
  level = "" 
  prerequisites = []
  summary = ""
  page_content.css('p').each do |p|
    t = find_split(p, 'Technologies')
    if t
      technologies = t
    end
    l = find(p, 'Level')
    if l
      level = l
    end
    pr = find_split(p, 'Prerequisites')
    if pr
      prerequisites = pr
    end
    s = find(p, 'Summary')
    if s
      summary = s
    end

  end
  dir = source_path[/([^\/]+)\/([^\/]+).md$/, 1]
  filename = source_path[/([^\/]+)\/([^\/]+).md$/, 2]
  if dir
    output = "<tr><td align='left'><a href='#{dir}/#{filename}.md' title='#{dir}'>#{dir}</td><td align='left'>#{' '.concat(technologies.map{|u| u} * ', ')}</td><td align='left'>#{summary}</td><td align='left'>#{level}</td><td align='left'>#{' '.concat(prerequisites.map{|u| u} * ', ')}</td></tr>\n"
    FileUtils.mkdir_p(File.dirname(toc_file))
    File.open(toc_file, 'a').write(output)
  end
end

def markdown(source_path)
  renderer = HTMLWithPygmentsAndPants.new(optionize [
    :with_toc_data,
    :xhtml
  ])
  markdown = Redcarpet::Markdown.new(renderer, optionize([
    :fenced_code_blocks,
    :no_intra_emphasis,
    :tables,
    :autolink,
    :strikethrough,
    :space_after_headers,
    :with_toc_data
  ]))
  text = source_path.read
  toc_file='dist/target/toc.html'
  if File.exist?(toc_file)
    qs_toc_content=File.open('dist/target/toc.html').read
    qs_toc = "<table><thead><tr><th align='left'><strong>Quickstart Name</strong></th><th align='left'><strong>Demonstrated Technologies</strong></th><th align='left'><strong>Description</strong></th><th align='left'><strong>Experience Level Required</strong></th><th align='left'><strong>Prerequisites</strong></th></tr></thead><tbody>#{qs_toc_content}</table></table>"
    text.gsub!("\[TOC-quickstart\]", qs_toc)
  end
  toc = Redcarpet::Markdown.new(Redcarpet::Render::HTML_TOC).render(text)
  text.gsub!("\[TOC\]", toc)
  rendered = markdown.render(text)
  metadata(source_path.path, rendered)
  rendered = rendered.gsub(/README.md/, "README.html").gsub(/CONTRIBUTING.md/, "CONTRIBUTING.html")
  '<html><head><title>README</title><link href="https://raw.github.com/pmuir/github-flavored-markdown/gh-pages/shared/css/documentation.css" rel="stylesheet"></link><link href="https://raw.github.com/github/github-flavored-markdown/gh-pages/shared/css/pygments.css" rel="stylesheet"></link></head><body>' + rendered + '</body></html>'
  end

def optionize(options)
  options.each_with_object({}) { |option, memo| memo[option] = true }
end

puts markdown(ARGF)

