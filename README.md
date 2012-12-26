# SimpleNote

It's a [SimpleNote](www.simplenoteapp.com) API Java wrapper.

## Dependencies

- [Apache HttpClient](http://hc.apache.org/downloads.cgi) - This is included in Android by default
- [Google Gson](http://code.google.com/p/google-gson/) - To handle the JSON responses and requests

Luckily you don't have to deal with this yourself. Thanks to Ant and Ivy you can execute:

        ant init

And all the dependencies will be downloaded and placed in the *lib* folder.

## Usage

Other commands are also available:

      ant samples - to compile the samples
      ant dist - to create the jar
      ant doc - generates the documentation
