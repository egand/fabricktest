#Introduzione
Questo applicativo implementa 3 servizi per la gestione delle seguenti operazioni su un conto passato in input
- Lettura saldo
- Lista delle transazioni
- Bonifico

I servizi sono stati implementati sfruttando le API messe a disposizione da Banca Sella.

Il progetto è realizzato in Java e Spring boot ed è gestito tramite l'uso di Maven. 
Per poter lanciare l'applicativo, importare il progetto come un progetto maven e scaricare le dipendenze presenti nel file pom.xml. 

All'interno del progetto è presente un webserver Tomcat integrato che, una volta avviato, permetterà di accedere ai servizi utilizzando come baseUrl localhost:8080.

Per il dettaglio degli endpoint, una volta fatto partire il webserver Tomcat è possibile visionare la documentazione swagger all'url **localhost:8080/swagger-ui.html**


Per il corretto utilizzo delle API messe a disposizione dalla banca, è necessario inserire una **ApiKey** e un **AuthSchema** valido all'interno del file di configurazione application-sb.properties.

All'interno del progetto è configurato un database in-memory H2 con all'interno una tabella dove possono essere immagazzinate le transazioni ricavate dall'API lista transazione. Al momento, non è stato possibile implementare l'inserimento a db in quanto l'API key messa  a disposizione è scaduta e non si riescono a ricavare le transazioni da inserire.