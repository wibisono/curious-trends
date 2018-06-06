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

*WARNING*

Still experimental:
- no persistence, no rate limit throttling so you might hit free rate limit 
- hashtags were just appended everytime you open another trends link
- trying to push data from backend to frontend for updates, but when tracking keywords that are not frequently mentioned you might endup with blank page with no data.

# Starting App

First configure twitter settings:
* Rename [twitter.conf.example](https://github.com/wibisono/curious-trends/blob/master/backend/src/main/resources/twitter.conf.example) into twitter.conf
* Setup your twitter API keys in that file.

Perpare the build with:

    sbt compileStatics
    
Then you can run:

    sbt run
    
    
http://localhost:9000/#/trends/[hashtag] to see current state of curious trend of [hashtag], should be pushing live update from backend. Everytime you open this endpoint with additional hashtag it will accumulate, and the more you did this you might hit twitter Rate limit for your API. Either that or memory limit at some point, current implementation has no backend/persistence support.

http://localhost:9000/assets/2d/  to see 2d visualization using d3 js. Improper integration with d3 js assuming you run this without modification on port 9000 on localhost. Still don't know how to properly use udash for this.

http://localhost:9000/assets/3d/  the 3d visualization. For both of these visualization, no live update/push from backend, it's pull based you need to refresh to get the latest data.


This is an example of normal generic trending topics user characteristics looks like (ramos after dirty wins of Real madrid):
![image](https://i.imgur.com/aq1KBgm.png)

Unfortunately I have not yet found another curious trends to be analysed using this newly build, work in progress, udash based analyser, what I currently have is just previous result of original gist that shows skewed user attributes that promote a certain tag, related to Indonesian politics (#HTILanjutkanPerjuangan):

![image](https://i.imgur.com/L4INuTr.gif)

Meanwhile, visualization of retweet graph using d3.js 3d force graph:

![image](https://i.imgur.com/QSBRKwB.jpg)
