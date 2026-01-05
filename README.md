# Click to Ten

Just click to ten.

## folder struture
```
clickToTen/
├── src/                # Java code
│   ├── App.java        # main entry point
│   └── util/SimpleHttpServer.java # HTTP server
├── img/                # image
├── music/              # music file
├── vid/                # the amimated file
├── html/               # html
├── bin/                # class file
├── lib/                # library (act nothing)
├── MANIFEST.MF         # Java manifest
└── README.md           # this document
```

## way to run

1. **compile**
	run
	```sh
	javac -d bin src/util/SimpleHttpServer.java src/App.java
	```
2. **execute**
	```sh
	java -cp bin App
	```
    or click jar file.

## How intereact
- click
- don't trust everything in it.

## requirement
- Java 24

## common issue
- nothing just white window
    make sure all resource is within your execute env.