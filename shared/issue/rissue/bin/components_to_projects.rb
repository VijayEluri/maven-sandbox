#!/usr/bin/ruby

require 'rubygems'
require_gem 'jiraruby'

im = IssueManager.new()

components = im.getComponents( "MNG" )

psid = 10010
nsid = 10001
baseKey = "MNGP"
nameSuffix = "Maven Plugin"
baseUrl = "http://maven.apache.org/plugins"

components.each do | c |
  if c.name =~ /plugin$/
    basename = c.name.sub( 'maven-', '' ).sub( '-plugin', '' ).gsub( '-', '' )
    puts "#{baseKey}#{basename.upcase},Maven 2.x #{basename.capitalize} Plugin,Maven 2.x #{basename.capitalize} Plugin,#{baseUrl}/#{basename},jason,#{psid},#{nsid}"
  end
end
