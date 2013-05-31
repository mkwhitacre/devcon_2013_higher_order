Scripts for metacritic.com
==========================

Description
-----------

[metacritic.com](http://www.metacritic.com/) is a website for collecting reviews on all 
sorts of media including video games, movies, books, etc. It even allows for user reviews.

Data
-------

### metacritic.com_raw.tar.gz

The tar file should look like,

    metacritic.com_processed.tar.gz
        |
        --- metacritic.com
        |
        --- --- ps3
        |
        --- --- --- a
        | 
        --- --- --- --- page_0.html
        |
        --- --- --- --- page_1.html ... (indefinte amount, it is not guranteed that there is even a page_0.html)
        |
        --- --- --- b
        |
        ...
        |
        --- --- --- z
        |
        --- --- xbox360
        |
        --- --- pc
        |
        --- --- wii-u
        |
        --- --- 3ds
        |
        --- --- vita
        |
        --- --- ios
        |
        --- --- wii
        |
        --- --- ds
        |
        --- --- psp

### metacritic.com_processed.tar.gz

JSON of metacritic's review data about all video games. 

The tar file should look like,

    metacritic.com_processed.tar.gz
    |
    --- metacritic.com
    |
    --- --- ps3_games.json
    |
    --- --- xbox360_games.json
    |
    --- --- pc_games.json
    |
    --- --- wii-u_games.json
    |
    --- --- 3ds_games.json
    |
    --- --- vita_games.json
    |
    --- --- ios_games.json
    |
    --- --- wii_games.json
    |
    --- --- ds_games.json
    |
    --- --- psp_games.json
    
Each JSON should be in the format,

    { [
        {"gameName" : NAME, "metacriticScore" : META_CRITIC_SCORE, "userScore" : USER_SCORE},
        ...
      ] 
    }

A `META_CRITIC_SCORE` can be things like, 10, 56, 97 or 'tbd' if it has not been decided.

A `USER_SCORE` can be things like, '1.0', '5.7', '9.8' or 'tbd' if it has not been decided.