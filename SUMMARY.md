# Summary

## Comments
This is one of the more fun assignments I have gotten from companies.
Not only having to learn a new syntax quickly, but also a new framework.

The  assignment was quite staight formward and was easy to follow.


## Which part of the assignment took the most time and why?

I have changed what code I have written at work enough times that getting used to kotlin was actually quite quick. 
But finding the correct functions to use was what took the most  amount of time. Having to google so much slowed me down, but as I got hang of things the speed increased.
Also I am pretty sure I made some dumb mistakes that a seaasoned Kotlin programmer would not have done :D. 

## What You learned

The basics of Kotlin for sure. Also how it kind of feels like a mix of Typescript and C++ in some ways.
Also learned about Kotlin exception handling and also how easy it is to build API-s using Spring Boot.

Honestly would love to have Spring Boot like for C++. I built API-s with QT-s
 libraries and they require a quite a bit more setup .

## TODOs

File validation is needed so tho the contentType field could be optional instead of mandatory.
The exception handling also needs to be better, with the throw commands moved out of the controller if possible.
The service functions (del, save, download, etc) could be split into smaller parts that could be used in some other code.
There are more little things as well and probably a lot I don't even know about. The language is new to me and so are the conventions of it.

##What the program does

Well it captures api calls and responds with the data as described in api doc.
The main files to look through are filecontroller and fileservice.
I split the del and upload etc, commands in the service files so they could be reused (for example later for bulk delete or etc)


## Starting the program

I did not use any extra libraries, so it should function without extra downloads.

This is started the same way as described in the readme

## Start-up

### Starting the database
    docker-compose up -d

### Configuration

See `variables.env` file

## Usage
In development add

    127.0.0.1    filedb
to your `/etc/hosts` file

For basic auth, username is `admin` and password is `hunter2`

### Start from CLI

    ./do.sh start

### Accessing the OpenAPI doc
	
	http://localhost:{port in config 6011 as default}/docs