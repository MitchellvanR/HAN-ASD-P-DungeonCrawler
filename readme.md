# 1. Het aanmaken van de installer

Open de dungeon-crawler projectmap.
Voer eerst `mvn clean` uit, en daarna `mvn package`.
Hierna zal er in de installer module onder de target map een bestand
genaamd `DungeonCrawler setup.jar` verschijnen.
Dit is de zojuist gemaakte installer.

# 2. Installeren van het spel

## 2.1. Installeer Java JDK versie 21

### 2.1.1. Windows

Installeer de x64 installer
via: https://www.oracle.com/nl/java/technologies/downloads/#jdk21-windows

### 2.1.2. Ubuntu

Open de terminal, en voer het volgende commando uit: `sudo apt install openjdk-21-jdk`.

### 2.1.3. MacOS

Installeer de ARM64 of x64 installer
via: https://www.oracle.com/nl/java/technologies/downloads/#jdk21-mac

## 2.2. Open de installer

### 2.2.1. Windows

Dubbelklik op de DungeonCrawler installer en doorloop de stappen.

### 2.2.2. Linux en MacOS

Open een terminal in dezelfde map als waar de installer staat, en voer het volgende commando
uit: `java -jar [installer]`.
Vervang [installer] met de daadwerkelijke bestandsnaam van de DungeonCrawler installer.

# 3. Hoe werkt de installer (voor devs)

In de installer module staat alle relevante code. De code om de installer zelf aan te maken staat
in `src/main/izpack`.
Hierin staan er vijf bestanden:

## 3.1. customicons.xml

Hierin wordt aangegeven dat er een andere afbeelding beschikbaar is voor de JFrameIcon.
Dit is het icoon dat de window van de installer heeft.
Bij H3.2.6 wordt hier meer over verteld.

## 3.2. install.xml

Alles voor de installer wordt hier gedefinieerd.

### 3.2.1. Info tag

Hier worden allerlei eigenschappen gedefinieerd, zoals de naam en de versie van de te installeren
applicatie.

### 3.2.2. Guiprefs tag

De afmetingen van de installer window worden hier gedefinieerd.

### 3.2.3. Locale tag

De beschikbare talen voor de installer worden hier gedefinieerd.

### 3.2.4. Listeners tag

Hierin worden listeners gedefinieerd.
In dit geval wordt hier aangegeven dat er bij het installeren van de applicatie op Windows een
wijziging in de registry moet komen.
Hierdoor verschijnt de applicatie na het installeren in de programma lijst, waar deze ook verwijderd
kan worden.
Ook wordt hier aangegeven dat diezelfde entry in de registry verwijderd moet worden als de
uninstaller uitgevoerd wordt.

### 3.2.5. Natives tag

Hier worden libraries geïmporteerd.
In dit geval wordt hier aangegeven dat de ShellLink.dll nodig is, deze zou volgens de documentatie
van IzPack vereist zijn om de ShortcutPanel op Windows correct te laten werken.
Ook wordt hier aangegeven dat de COIOSHelper.dll nodig is, deze is vereist om de RegistryListeners (
die in het hoofdstuk hierboven beschreven staan) te laten werken.
Door hierbij de `uninstaller` property op `true` te zetten wordt deze library ook bij de uninstaller
geïmporteerd.

### 3.2.6. Resources tag

Bij sommige panels kunnen er resources toegevoegd worden.
Ook worden sommige XML bestanden hier als resources aan de installer toegevoegd.
Het doel van elk XML bestand wordt per bestand in een aparte tekst beschreven.

- JFrameIcon: Hier wordt de nieuwe JFrameIcon gedefinieerd. Dit wordt ook in customicons.xml
  gebruikt.
- Installer.image: Dit is de afbeelding dat op elke panel in de installer links getoond wordt.
- LicencePanel.licence: Dit is de licentie dat op de LicencePanel getoond wordt.

### 3.2.7. Panels tag

De gebruikte panels worden hier op volgorde gedefinieerd.
Voor mijn gevoel is de werking hiervan best vanzelfsprekend.
In de documentatie van IzPack wordt er beschreven welke panels er zijn, en wat de panels doen.

### 3.2.8. Packs tag

Hier wordt alles wat geïnstalleerd moet of kan worden gedefinieerd.
Dit elk installeerbaar stukje is een pack.
Door middel van een PacksPanel kan je de gebruiker laten kiezen welke packs die wilt installeren.
Omdat er in dit geval geen optionele stukken zijn, wordt er geen PacksPanel gebruikt.
Er is alleen een verplichte pack, en dat is het spel zelf.

## 3.3. installerImage.png

Een mooie AI-gegenereerde afbeelding dat in de install.xml als `Installer.image` gebruikt wordt.
Dit wordt kort in H3.2.6 beschreven.

## 3.4. license.txt

Dit is de licentie dat in de LicencePanel gebruikt wordt.
Hoe dat werkt wordt kort in H3.2.6 beschreven.

## 3.5. (Unix_)shortcutSpec.xml

ShortcutSpec staat voor Shortcut Specification.
In deze twee bestanden worden de door de installer aan te maken snelkoppelingen gedefinieerd.
Het bestand dat met `Unix_` begint wordt voor besturingssystemen zoals Linux en MacOS gebruikt, en
het andere bestand wordt voor Windows gebruikt.