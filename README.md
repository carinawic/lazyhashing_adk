# Lazyhashing
## ADK DD2350

# notes:

Ubuntu program for "korpus":
grep -B 5 " a " textfile.txt

## Process:

Vi har ordet “BARA”

### Steg 1: Hashning

Vi tar de 3 första bokstäverna, det blir “BAR”. Vi räknar ut vad hashen är för BAR enligt formeln
(notera att vi konverterar strängen “i” till “i  ”, fyller ut med mellanslag)

W = “BAR”
hash(w) = f[w[0]]*900 + f[w[1]]*30 + f[w[2]]


### Steg 2: Indexarray A

Input = hashen av “bar”.

Vi stoppar in hashen av “bar” som index i Array A och får ut ett tal. Array A är lika lång som alla hashar av AAA-ÖÖÖ = 30^3. Värdena i listan motsvarar indexet i Tabell B för platsen på det första ordet som börjar på “bar”.

A[hash(“BAR”)] = 455

Siffran 455 anger att det första ordet som börjar på BAR i Tabell B finns på tabellindex 455 (dvs, om ordet “BARA” är det första ordet på “BAR” då är det 455:e ordet i tabell B)

Vi gör samma sak för A[hash(“BAR+1”)] dvs. A[hash(“BAS”)]
Vi får då att A[hash(“BAS”)] har sin första occurrence på index 600]

Output = 455, 600


### Steg 3:

Tabell B är en jättestor tabell som har ALLA bokens ord i bokstavsordning. Vi vet att ordet “BARA” finns på plats 455, och att ordet “BAS” finns på plats 600.

Vi gör en binärsökning i tabell B mellan (min, max) = (455, 600) tills vi hittar order “bara”.

Tabell B är en map, som mappar ord med platser, så när vi har hittat ordet “bara” så är key=“bara” och value= alla index i boken där ordet “bara” förekommer.
