# Dejumbler

A word unscrambler application

## Setup

Running this application requires a list of English words in text format. I use the list found in the [wamerican-insane](https://packages.ubuntu.com/focal/wamerican-insane) package. First, install the package (command below is for Ubuntu systems):

```bash
sudo apt-get install wamerican-insane
```

Then, copy the word list to the project.

```
cp /usr/share/dict/american-english-insane core/src/main/resources
```

`american-english-insane` is ignored in the project's .gitignore.
