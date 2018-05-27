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

First configure twitter API keys rename [twitter.conf.example](https://github.com/wibisono/curious-trends/blob/master/backend/src/main/resources/twitter.conf.example) into twitter.conf and set your keys there, then:

    sbt run
    
http://localhost:9000/#/trends to see current state of curious trend.


This is an example of normal generic trending topics user characteristics looks like (ramos after dirty wins of Real madrid):
![image](https://i.imgur.com/aq1KBgm.png)

Unfortunately I don't yeat have a sample of curious trending topic using the new UI, but here was a sample output of the original gist, tracking a curiously promoted hashtag #HTILanjutkan (Indonesian politics):
![image](https://i.imgur.com/L4INuTr.gif)
