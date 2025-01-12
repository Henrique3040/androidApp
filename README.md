# Locaties Opslaan App

## Overzicht
Deze Android-applicatie biedt gebruikers de mogelijkheid om locaties te zoeken via de Google Places API, deze te selecteren en op te slaan om later te onthouden. De opgeslagen locaties kunnen worden bekeken en indien nodig verwijderd via een aparte pagina. Voor het opslaan van de locaties worden zowel Room (lokaal) als Firebase (cloud) gebruikt.

---

## Functionaliteiten

### **MainPage**
- Gebruiker kan een locatie zoeken met behulp van de Google Places API.
- Mogelijkheid om de geselecteerde locatie op te slaan.

### **SavedLocatie**
- Een lijst met alle opgeslagen locaties wordt weergegeven.
- Gebruiker kan locaties verwijderen uit de opgeslagen lijst.

---

## Technologieën
- **Kotlin**: Voor het ontwikkelen van de app.
- **Firebase**: Voor cloud-gebaseerde opslag.
- **Google Places API**: Voor het zoeken en selecteren van locaties.

---

## Instructies voor installatie

### Belangrijke stappen om de code te laten werken:

1. **API-sleutel aanmaken voor Google Places**
   - Ga naar de Google Cloud Console.
   - Maak een nieuw project aan (of gebruik een bestaand project).
   - Activeer de Google Places API en genereer een API-sleutel.

2. **API-sleutel toevoegen aan lokaal.properties**
   - Voeg de volgende regel toe aan het bestand `lokaal.properties` in je project:
     ```
     MAPS_KEY="JOUW_API_SLEUTEL"
     ```

3. **Firebase instellen**
   - Maak een Firebase-project aan via [Firebase Console](https://console.firebase.google.com/).
   - Volg deze [tutorial](https://www.youtube.com/watch?v=KSG2METyPMs) om Firebase in je Android-app te integreren.

---

## Hulpbronnen

### **YouTube-video's**

1. Firebase-integratie: [Bekijk hier](https://www.youtube.com/watch?v=KSG2METyPMs)
2. Meertalige ondersteuning: [Bekijk hier](https://www.youtube.com/watch?v=ObgmK3BywKI&t=232s)
3. Google Places API: [Bekijk hier](https://www.youtube.com/watch?v=q9aCduNNkI8&t=1s)
4. UI-ontwikkeling:
   - (https://www.youtube.com/watch?v=_gDK4r_0x9Y)
   - (https://www.youtube.com/watch?v=HmXgVBys7BU)
   - (https://www.youtube.com/watch?v=0xtrtRstrLA)

### **Chat Logs voor ondersteuning**
- [Chatlog 1](https://chatgpt.com/share/67844581-ba30-8009-acd6-52867e818727)
- [Chatlog 2](https://chatgpt.com/share/678445bb-a960-8009-982b-461b5b0d3fd8)
- [Chatlog 3](https://chatgpt.com/share/6784464c-37c8-8009-b198-67c244597945)
- [Chatlog 4](https://chatgpt.com/share/6784466d-4144-8009-8e23-94dcd8ccf8b5)
- [Chatlog 5](https://chatgpt.com/share/678446f5-9208-8009-b119-433a2b256878)

---

## Licentie
Dit project heeft geen specifieke licentie en is bedoeld als proof-of-concept voor educatieve doeleinden.
