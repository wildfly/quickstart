#!/usr/bin/env ruby
# Processor for Github flavored markdown, inspired by:
# https://github.com/alampros/Docter/blob/master/bin/github-flavored-markdown.rb
#
# Current version of this script can be found here:
# https://gist.github.com/1300939
#
# Adapted for Redcarpet version 2 by Ralph von der Heyden
# http://github.com/ralph
# http://twitter.com/ralph
# http://www.rvdh.de
#
# You will need the following gems:
# * redcarpet version 2
# * albino
#
# You also need the pygments library for syntax highlighting:
# sudo easy_install pygments
# Make sure the pygmentize bin is in your $PATH, or link it like that:
# sudo ln -s /usr/local/bin/pygmentize /usr/bin
#
# Make sure to chmod +x this script.
#
# Usage:
# github-flavored-markdown.rb document.md

require 'rubygems'
require 'redcarpet'
require 'pygments.rb'

# create a custom renderer that allows highlighting of code blocks
class HTMLWithPygmentsAndPants < Redcarpet::Render::HTML
  include Redcarpet::Render::SmartyPants
  def block_code(code, language)
    Pygments.highlight(code, :lexer => language, :options => {:encoding => 'utf-8'})
  end
end

def markdown(text)
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
  toc = Redcarpet::Markdown.new(Redcarpet::Render::HTML_TOC).render(text)
  text.gsub!("\[TOC\]", toc)
  rendered = markdown.render(text)
  rendered = rendered.gsub(/README.md/, "README.html").gsub(/CONTRIBUTING.md/, "CONTRIBUTING.html")
  '<html><head><title>README</title><link href="https://raw.github.com/pmuir/github-flavored-markdown/gh-pages/shared/css/documentation.css" rel="stylesheet"></link><link href="https://raw.github.com/github/github-flavored-markdown/gh-pages/shared/css/pygments.css" rel="stylesheet"></link></head><body>' + rendered + '</body></html>'
  end

def optionize(options)
  options.each_with_object({}) { |option, memo| memo[option] = true }
end

puts markdown(ARGF.read)

