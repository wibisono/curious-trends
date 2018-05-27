# Curious Trends

Analysing curious trending topic based on an existing [gist](https://gist.github.com/wibisono/25a5e43d380c08afd35813672a5641b2) that observes user characteristics of a certain trending topic/hashtags. Some indication of trending hash tag is considered curious:
- mainly promoted by relatively new accounts 
- most of them are having few followers
- some of them tweet an unusual amount of tweet per user

All of these indicator are just a hunch, no solid base.

Built using:
* [udash.io](http://udash.io) framework
* [udash g8](https://github.com/UdashFramework/udash.g8) scaffolding as starting point.
* [twitter4s](https://github.com/DanielaSfregola/twitter4s) for twitter client.

# Starting App

First configure twitter settings:
* Rename [twitter.conf.example](https://github.com/wibisono/curious-trends/blob/master/backend/src/main/resources/twitter.conf.example) into twitter.conf
* Setup your twitter API keys in that file, and unfortunately hardcoded hashtag you wanted to track for now.

Then you can run:

    sbt run
    
    
http://localhost:9000/#/trends to see current state of curious trend.


This is an example of normal generic trending topics user characteristics looks like (ramos after dirty wins of Real madrid):
![image](https://i.imgur.com/aq1KBgm.png)

Unfortunately I have not yet found another curious trends to be analysed using this newly build, work in progress, udash based analyser, what I currently have is just previous result of original gist that shows skewed user attributes that promote a certain tag, related to Indonesian politics (#HTILanjutkanPerjuangan):

![image](https://i.imgur.com/L4INuTr.gif)
