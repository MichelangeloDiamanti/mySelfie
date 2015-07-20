# mySelfie

mySelfie - be yourselfie

L'applicazione è stata sviluppata su macchine linux, si è quindi preso in considerazione
tale filesystem ed il relativo formato dei path UNIX. E' stata inoltre testata su sistemi
Linux (in particolare Arch Linux).

Configurazione HTTPS:
L'applicazione è configurata per reindirizzare le richieste HTTP alla porta 8443 (tomcat HTTPS)
è quindi necessario generare un certificato self-signed grazie al toolkit java, (http://goo.gl/A6spg7)

Configurazione directories:
L'applicazione si serve della home dello user per accedere alle risorse salvate (selfie, immagini di profilo, etc...)
E' quindi necessario creare la seguente struttura delle directory nella propria home:
	
	mySelfie
	├── resources
	│   ├── profilepics
	│   ├── selfies
	│   │   ├── compressedSize
	│   │   └── originalSize
	│   └── tmp
	└── tempCredentials