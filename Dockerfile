# === ÉTAPE 1: Build de l'application ===
# On part d'une image contenant Maven et Java 17 pour compiler notre code.
FROM maven:3.9.6-eclipse-temurin-17 AS build

# On définit le répertoire de travail à l'intérieur de l'image.
WORKDIR /app

# On copie d'abord le pom.xml pour que Docker puisse mettre en cache la couche des dépendances.
COPY pom.xml .
# On télécharge les dépendances.
RUN mvn dependency:go-offline

# On copie le reste du code source.
COPY src ./src

# On compile le code, on crée le .jar, et on saute les tests (déjà faits dans le pipeline).
RUN mvn clean package -DskipTests


# === ÉTAPE 2: Création de l'image finale ===
# On part d'une image légère contenant uniquement l'environnement d'exécution Java (JRE).
FROM eclipse-temurin:17-jre-jammy

# On définit le répertoire de travail.
WORKDIR /app

# On copie uniquement le fichier .jar compilé depuis l'étape de build.
# C'est ce qui rend notre image finale beaucoup plus petite et sécurisée.
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# On expose le port sur lequel l'application Spring Boot tourne par défaut.
EXPOSE 8080

# C'est la commande qui sera exécutée au lancement du conteneur.
ENTRYPOINT ["java", "-jar", "app.jar"]
