INSTALLAZIONE
Setup ambiente
Per poter eseguire CINEMAX, è necessario installare sul computer il Java Development Kit (JDK) 25 o superiore. Se si ha già un JDK installato, verificare la versione aprendo il terminale e digitando:

java -version

Installazione programma
Trattandosi di un'applicazione eseguibile da interfaccia a riga di comando, il software non richiede procedure di installazione automatizzate nel sistema operativo, ma si basa sul posizionamento e sull'estrazione dei file di progetto.
La procedura di installazione e configurazione iniziale si articola nei seguenti passaggi:
- Download: accedere al repository remoto tramite il link ufficiale di GitHub del progetto e procedere al download del pacchetto dell'applicazione, rilasciato sotto forma di file ZIP.
- Estrazione del pacchetto: localizzare l'archivio scaricato sul proprio computer ed estrarlo. L'operazione genererà la cartella Laboratorio-Interdisciplinare-A-2025-2026.


ESECUZIONE ED USO
Per lanciare l'applicazione, il terminale deve essere posizionato all'interno della cartella principale del programma, in modo da consentire a Java di rintracciare i file di configurazione. Si può orientare il terminale seguendo una di queste due modalità:

- Modalità 1: aprire la cartella principale Laboratorio interdisciplinare 2025-2026, fare click con il tasto destro del mouse in un punto vuoto e selezionare la voce "Apri nel terminale" (o "Apri terminale qui" / "Apri in Terminale Windows" a seconda del sistema operativo).

- Modalità 2: aprire la cartella principale Laboratorio interdisciplinare 2025-2026 e copiare il percorso dalla barra degli indirizzi in alto. Successivamente, aprire il terminale, digitare il comando cd seguito da uno spazio e incollare il percorso copiato (ad esempio cd C:\Utenti\Desktop\Laboratorio-Interdisciplinare-A-2025-2026) e premere Invio.

A questo punto si può avviare l'eseguibile posizionato nella cartella bin con uno dei seguenti metodi:
Digitazione con auto completamento: digitare il comando base puntando alla sotto-cartella:
java -jar bin/C
A questo punto, è sufficiente premere il tasto TAB sulla tastiera: il terminale completerà automaticamente il nome del file in java -jar bin/Cinemax.jar.

Trascinamento del file: digitare nel terminale il comando java -jar seguito da uno spazio. Aprire separatamente la cartella bin, selezionare il file Cinemax.jar con il mouse e trascinarlo direttamente all'interno della finestra del terminale.
Copia e incolla del file: digitare nel terminale java -jar seguito da uno spazio. Andare nella cartella bin, fare click con il tasto destro sul file Cinemax.jar e selezionare "Copia" . Tornare sul terminale e incollare.
Infine, premere il tasto Invio della tastiera per eseguire il comando e visualizzare a schermo il Menu Iniziale.
