Progetto realizzato da Pasta Roberto 869247
- DESCRIPTION:
E' stata creata una classe Registry che opera su una porta nota (3000); la classe gestisce, tramite una HashMap, le informazioni per poter raggiungere un determinato servizio,
la chiave usata per la HashMap è il nome del servizio (deve essere univoco nel Registry) e ogni chiave ha una istanza della classe ServiceData collegata; questa classe gestisce le triple
di valori che deve possedere ogni servizio (IP, Porta, Protocollo).
Nel package Server sono presenti una classe ServiceServer che crea servizi generici e li salva nel Registry e una classe ServerBanca (eredita da ServiceServer) che possiede una HashMap, questa
identifica ogni ContoCorrente con un numero univoco e gestisce questi conti tramite metodi sincronizzati (preleva, versa, creaConto e bonifico); è presente una classe contoCorrente che tiene
conto del credito e dei titolari del conto.
Nel package Client è presente una classe ClientService che recupera le informazioni di servizi generici selezionati dal Registry e le stampa; è presente una classe ClientBancaMain (implemeta Runnable) 
che tramite un Thread inserisce operazioni in un buffer (classe OperationBuffer) e nel main esegue queste operazioni.
La comunicazione è realizzata tramite lo scambio di una stringa alla volta che verrà poi separata per estrarre le operazione specificate grazie a dei caratteri separatori (ex. ";" e ",").



- TESTING:
Per poterlo testare bisogna anzitutto lanciare la classe Registry.
Successivamente lanciando la classe ServerMain verranno inizializzati più server (ServiceServer) che rappresentano servizi differenti generici e salveranno le informazioni di ip, porta 
e protocollo nel Registry e un server della banca (ServerBanca) che si registrerà anch'esso al Registry e entrarà in un loop infinito da cui lancierà i thread slave per poter soddisfare 
le richieste dei client.
Fatto ciò è possibile lanciare la classe ClientService che inizializza un client, questo chiede randomicamente al Registry le informazioni del server per un servizio specifico (nomi dei servizi 
presi da un pool di nomi preimpostato) e stampa a terminale il risultato.
Infine per poter interagire con il Server della banca bisogna lanciare la classe ClientBancaMain che per 10 secondi comunicherà le operazioni da svolgere al Server, inserite in un buffer 
da un thread lanciato dal Main, per poi terminare.
I valori delle operazioni di prelievo, deposito e bonifico sono tutti presi randomicamente da un intervallo tra 1-100.