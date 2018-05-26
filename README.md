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

    sbt run
    
http://localhost:9000/#/trends to see current state of curious trend.

http://localhost:9000/#/chat to see default chat application from Udash g8 template.

