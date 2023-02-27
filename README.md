Si desidera realizzare il sistema distribuito costituito da vari server e client. I server e i client possono
risiedere su macchine diverse.
Bisogna realizzare un meccanismo che permetta a dei server che offrono dei servizi (identificati
da un nome) di registrare quei servizi presso un Registry situato su una macchina nota. I client
possono accedere al Registry, cercare il server che offre un certo servizio, recuperarne le
informazioni per contattarlo, e quindi aprire un canale con quel server per richiedere il servizio.
Tutte le comunicazioni devono essere implementate utilizzando socket o channels.
Il sistema deve prevedere quindi le seguenti entità: un server Registry, 'n' Server (in grado di offrire i
servizi richiesti nel testo più avanti), 'm' Client che desiderino utilizzare quei servizi.
