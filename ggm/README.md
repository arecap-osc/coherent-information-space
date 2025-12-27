# Global Governance Model (GGM)

Sistemul GGM (dezvoltat inițial în 2018–2019) reprezintă o platformă modulară Java care expune un set de microservicii Spring Boot și interfețe Vaadin pentru a orchestra un model de guvernanță globală centrat pe date. Structura de mai jos descrie rolul fiecărei componente și modul în care acestea colaborează.

## Vedere de ansamblu a arhitecturii
- **Biblioteci comune (`libraries/`)** – module Maven partajate (root/build/modules) care expun capabilități de persistență, calcul și servicii de bază.
- **Microservicii (`microservices/`)** – servicii Spring Boot containerizabile (au Dockerfile și manifest YAML) care compun API‑urile și UI‑urile sistemului.
- **Front‑end Vaadin** – console web pentru operatori și feedback de utilizator, construite peste Vaadin Spring 13 + Spring Boot 2.0.x.
- **Observabilitate și deployment** – dependințe Micrometer pentru Datadog (media API), generare `git.properties` prin `git-commit-id-plugin`, livrare în Artifactory și manifest Kubernetes pentru medii dev/prod.

## Structura proiectului și roluri
### Biblioteci partajate
- `eden-ggm-libraries-root` / `eden-ggm-libraries-ebom` / `eden-ggm-libraries-build` / `eden-ggm-libraries-modules` – stratul de orchestrare Maven care aliniază versiunile, codul sursă comun și regulile de build (Java 8, checkstyle).【F:libraries/eden-ggm-libraries-root/pom.xml†L1-L63】
- `eden-ggm-data-repository` – modelul de date și accesul la persistență (Spring Data JPA/Envers, Hibernate), reutilizat de microserviciile JPA și UI‑urile Vaadin.【F:libraries/eden-ggm-data-repository/pom.xml†L1-L74】
- `eden-ggm-services` – servicii de domeniu și utilitare partajate (Spring Context, Security, JWT) pe care se sprijină API‑urile și consolele Vaadin.【F:libraries/eden-ggm-services/pom.xml†L24-L75】
- `eden-ggm-algebraic-fractals` – librărie pentru calcule/algoritmi specifici domeniului GGM, folosită de depozitul de date.【F:libraries/eden-ggm-algebraic-fractals/pom.xml†L1-L24】

### Microservicii
- `eden-ggm-media-api` – API media Spring Boot (REST + JPA) cu suport JWT, PostgreSQL, metrici Micrometer/Datadog și artefact Docker/Kubernetes pentru livrare în medii dev/prod.【F:microservices/eden-ggm-media-api/pom.xml†L1-L74】【F:microservices/eden-ggm-media-api/pom.xml†L80-L113】
- `eden-ggm-netting-provider` – serviciu backend care folosește `eden-ggm-data-repository` și PostgreSQL pentru calcule și netting de date guvernanță; include versionare git în build.【F:microservices/eden-ggm-netting-provider/pom.xml†L1-L75】【F:microservices/eden-ggm-netting-provider/pom.xml†L93-L127】
- `eden-ggm-vaadin-console` – consolă de administrare bazată pe Vaadin (Spring 13) cu FF4J pentru feature flags, Flyway pentru migrații și integrare cu depozitul de date.【F:microservices/eden-ggm-vaadin-console/pom.xml†L9-L66】【F:microservices/eden-ggm-vaadin-console/pom.xml†L66-L117】
- `eden-ggm-vaadin-feedback-grade1` – UI de colectare feedback/grade cu Vaadin Flow, securizare Auth0 + Spring Security și suport pentru import/export Excel (Apache POI).【F:microservices/eden-ggm-vaadin-feedback-grade1/pom.xml†L1-L71】【F:microservices/eden-ggm-vaadin-feedback-grade1/pom.xml†L73-L123】
- `eden-ggm-system-vaadin` – interfață Vaadin suplimentară pentru operațiuni de sistem și configurare, bazată pe Spring Boot 2.0.5 și plugin de build info (git).【F:microservices/eden-ggm-system-vaadin/pom.xml†L1-L64】【F:microservices/eden-ggm-system-vaadin/pom.xml†L64-L116】
- `eden-ggm-web-resources` – serviciu Spring Boot minim pentru livrarea de resurse front‑end (WebJars jQuery, panzoom, canvas worker) reutilizabile de celelalte UI‑uri.【F:microservices/eden-ggm-web-resources/pom.xml†L1-L66】

## Flux de lucru și interacțiuni
1. **Bibliotecile** sunt publicate în Artifactory (snapshot/release) și adăugate ca dependențe în microservicii, asigurând modele de date și servicii consistente.
2. **Microserviciile API (media, netting)** expun REST și JPA către PostgreSQL, autentificare cu JWT/Auth0 și metrici de sănătate/observabilitate.
3. **UI‑urile Vaadin** consumă aceste API‑uri și prezintă operatorilor console de administrare, feedback și configurare; folosesc `eden-ggm-data-repository` direct acolo unde este necesar pentru acces rapid la date.
4. **Livrare**: fiecare serviciu poate fi construit cu Maven (`mvn clean package`) și împachetat în container prin `Dockerfile`, apoi orchestrat cu manifestele Kubernetes incluse (ex. `kube_edenggmmediaapi_dev.yaml`).【F:microservices/eden-ggm-media-api/Dockerfile†L1-L13】【F:microservices/eden-ggm-media-api/kube_edenggmmediaapi_dev.yaml†L1-L28】

## Pași rapizi de dezvoltare
1. **Prerechizite**: Java 8 și Maven 3.5+ (mvnw este inclus în microservicii).【F:microservices/eden-ggm-media-api/pom.xml†L16-L33】【F:libraries/eden-ggm-libraries-root/pom.xml†L30-L37】
2. **Build biblioteci**: din `libraries/`, rulați `mvn clean install` pentru a publica artefactele locale.
3. **Build serviciu**: în directorul microserviciului dorit, rulați `./mvnw clean package` (sau `mvn` dacă mvnw nu este disponibil) pentru a genera jar‑ul Spring Boot.
4. **Containerizare/deployment**: folosiți `docker build` cu Dockerfile‑ul serviciului și aplicați manifestul Kubernetes corespunzător mediului (dev/prod).
5. **Observabilitate**: activați exportul Micrometer Datadog acolo unde este configurat (ex. `eden-ggm-media-api`) și includeți `git.properties` în build pentru trasabilitate de versiune.

## Context și autorat
Proiectul a fost inițiat de HKRDI/ARECAP (Octavian Stirbei – project lead) în perioada 2018–2019; autorii și rolurile sunt menționate în POM‑urile modulelor principale.【F:libraries/eden-ggm-services/pom.xml†L34-L55】【F:libraries/eden-ggm-data-repository/pom.xml†L18-L49】 README‑ul oferă o vedere executivă pentru orientare rapidă asupra componentelor și fluxurilor din sistem.
