# 🧠 GraphQL Workshop — Service-Oriented Architecture (SOA)


---

## 🎯 Workshop Objectives

The main goal of this workshop is to **design and develop a GraphQL web service** for managing the university’s academic modules.

By the end of the workshop, students will be able to:

1. Understand the difference between **REST and GraphQL** (philosophy, advantages, and limitations).  
2. Model the **GraphQL schema** representing the business entities.  
3. Implement **GraphQL operations** (queries and mutations).  
4. Set up a **GraphQL API using a Java backend**.  
5. Test and consume the API using **Postman** or **GraphiQL**.

---

## 🏗️ Project Context

The university aims to modernize its academic management system to improve the experience for students and faculty members.  
The project focuses on centralizing data and exposing core functionality through a **GraphQL API**, as an evolution from REST services.

The main entities are:

- **Module**  
- **Teaching Unit (Unité d’Enseignement / UE)**  

An enumeration is used to classify module types:

| TypeModule | Description |
|-------------|-------------|
| **TRANSVERSAL** | Common to several programs (e.g., Languages) |
| **PROFESSIONAL** | Focused on professional skills (e.g., projects, internships) |
| **RESEARCH** | Dedicated to research activities (e.g., dissertations, theses) |

---

## ⚙️ Features to Implement

### 🧩 A. Modules

1. Retrieve the list of all modules.  
2. Create a **new module** (returns `Boolean`).  
3. Update an existing module (by ID).  
4. Retrieve modules by **type**.  
5. Retrieve a module by **matricule**.  
6. Delete a module by **matricule**.

---

### 🧱 B. Teaching Units (UE)

1. Create a **new teaching unit**.  
2. Retrieve a teaching unit by its **unique code**.  
3. Retrieve all teaching units for a given **domain**.  
4. Retrieve all teaching units for a given **semester**.  
5. Update an existing teaching unit (by code).  
6. Delete a teaching unit (by code).

---

## 🧮 Task Requirements

A preconfigured Java project is provided, containing:

- **Entity classes** (`Module`, `Unite_enseignement`, etc.)  
- **Business classes** implementing domain logic

You are required to:

1. **Implement GraphQL queries and mutations** as described above.  
2. **Define the GraphQL schema** (`schema.graphqls`) representing the data model.  
3. **Configure the GraphQL server**   
4. **Test operations** using Postman or GraphiQL.  
5. **Observe and explain** what happens when multiple queries are executed within the same GraphQL request.

---


## 📚 Project Structure

```
📦 graphql-workshop
 ┣ 📂 src
 ┃ ┣ 📂 main
 ┃ ┃ ┣ 📂 java
 ┃ ┃ ┃ ┣ 📂 com.esprit.graphql
 ┃ ┃ ┃ ┃ ┣ 📂 entity
 ┃ ┃ ┃ ┃ ┣ 📂 repository
 ┃ ┃ ┃ ┃ ┣ 📂 graphql
 ┃ ┃ ┃ ┃ ┣ 📜 GraphQLEndPoint.java
 ┃ ┃ ┣ 📂 resources
 ┃ ┃ ┃ ┣ 📜 schema.graphqls
 ┣ 📜 pom.xml
 ┣ 📜 README.md
```

---

## 🚀 Implementation Steps

1. **Analyze** the existing entities and business logic.  
2. **Create GraphQL resolvers**:  
   - `QueryResolver` for read operations  
   - `MutationResolver` for write operations  
3. **Define the GraphQL schema** (`schema.graphqls`).  
4. **Configure the GraphQL server** within Spring Boot.  
5. **Test** queries and mutations using Postman or GraphiQL.  
6. **Document** your tests and observations.

---

---

## 🧪 Comprehensive GraphQL Operations & Postman Collection

Endpoint URL: `http://localhost:8083/WorkshopGraphQL_war_exploded/graphql` (or your configured port/context path).

### 🧩 A. Modules Operations

#### 1. Récupération de la liste de tous les modules
```graphql
query {
  allModules {
    matricule
    nom
    volumeHoraire
    type
    uniteEnseignement {
      code
      domaine
      responsable
    }
  }
}
```

#### 2. Création d'un nouveau module avec un type de retour Boolean
```graphql
mutation {
  addModule(
    matricule: "UE123"
    libelle: "SOA"
    coef: 2
    volumeHoraire: 21
    codeUE: 1
    type: TRANSVERSAL
  )
}
```
**Response:**
```json
{
  "data": {
    "addModule": true
  }
}
```

#### 3. Modification d'un module ayant un identifiant spécifique
```graphql
mutation {
  updateModule(
    matricule: "M101"
    libelle: "Algorithmique"
    coef: 3
    volumeHoraire: 21
    type: PROFESSIONNEL
    codeUE: 1
  ) {
    matricule
    nom
    volumeHoraire
    type
  }
}
```
**Response:**
```json
{
  "data": {
    "updateModule": {
      "matricule": "M101",
      "nom": "Algorithmique",
      "volumeHoraire": 21,
      "type": "PROFESSIONNEL"
    }
  }
}
```

#### 4. Récupération de la liste des modules ayant un type spécifique
```graphql
query {
  getModulesByType(type: PROFESSIONNEL) {
    matricule
    nom
    volumeHoraire
    type
  }
}
```

#### 5. Récupération d'un module ayant une matricule spécifique
```graphql
query {
  getModuleByMatricule(matricule: "M101") {
    matricule
    nom
    volumeHoraire
    type
    uniteEnseignement {
      code
      domaine
    }
  }
}
```

#### 6. Suppression d'un module ayant une matricule spécifique
```graphql
mutation {
  deleteModule(matricule: "M102")
}
```
**Response:**
```json
{
  "data": {
    "deleteModule": "deleted successfully"
  }
}
```

---

### 🧱 B. Unités d'Enseignement (UE) Operations

#### 1. Création d'une nouvelle unité d'enseignement
```graphql
mutation {
  addUniteEnseignement(
    code: 10
    domaine: "Intelligence Artificielle"
    responsable: "Dr. Ali"
    credits: 5
    semestre: 2
  )
}
```
**Response:**
```json
{
  "data": {
    "addUniteEnseignement": true
  }
}
```

#### 2. Récupération d'une Unité d'Enseignement à partir de son code unique
```graphql
query {
  getUEByCode(code: 1) {
    code
    domaine
    responsable
    credits
    semestre
  }
}
```

#### 3. Récupération de toutes les Unités d'Enseignement appartenant à un domaine donné
```graphql
query {
  getUEByDomaine(domaine: "Informatique") {
    code
    domaine
    responsable
    credits
  }
}
```

#### 4. Récupération de toutes les Unités d'Enseignement d'un semestre donné
```graphql
query {
  getUEBySemestre(semestre: 1) {
    code
    domaine
    responsable
    credits
    semestre
  }
}
```

#### 5. Mettre à jour une Unité d'Enseignement existante (identifiée par son code)
```graphql
mutation {
  updateUniteEnseignement(
    code: 1
    domaine: "Informatique Avancée"
    responsable: "Mme Maroua Douiri"
    credits: 6
    semestre: 1
  )
}
```

#### 6. Supprimer une Unité d'Enseignement en fonction de son code
```graphql
mutation {
  deleteUniteEnseignement(code: 5)
}
```

---

### ❓ Question 3 : Tester plusieurs opérations query dans une seule requête GraphQL, que remarquez-vous ?

#### Requête test multi-query :
```graphql
query GetAcademicOverview {
  # 1. Récupération de tous les modules
  allModules {
    matricule
    nom
    type
  }
  
  # 2. Récupération de toutes les UE
  allUEs {
    code
    domaine
    responsable
  }
  
  # 3. Récupération d'une UE spécifique avec un alias
  ueInformatique: getUEByCode(code: 1) {
    code
    domaine
    credits
  }
  
  # 4. Récupération des modules professionnels
  modulesPro: getModulesByType(type: PROFESSIONNEL) {
    matricule
    nom
  }
}
```

#### Ce que l'on remarque (Observations & Analyse) :
1. **Une seule requête HTTP (Single Round-Trip)** :
   Contrairement à une architecture REST où il faudrait effectuer **4 requêtes HTTP distinctes** (`GET /modules`, `GET /ues`, `GET /ues/1`, `GET /modules?type=PROFESSIONNEL`), GraphQL permet de regrouper toutes ces demandes en **un seul appel HTTP**. Cela réduit drastiquement la latence réseau et la charge sur le serveur.
2. **Réponse consolidée et structurée** :
   Le serveur renvoie une seule charge utile JSON sous l'objet `"data"` où chaque clé correspond exactement aux opérations demandées.
3. **Absence de sur-récupération (Over-fetching) et de sous-récupération (Under-fetching)** :
   Pour chaque sous-requête, le client spécifie exactement les champs dont il a besoin (ex: seulement `matricule` et `nom`), évitant tout transfert de données inutiles.
4. **Utilisation des alias** :
   Lorsqu'on appelle deux fois la même opération ou pour clarifier la réponse (ex: `ueInformatique: getUEByCode(code: 1)`), GraphQL permet de renommer les clés de sortie pour éviter toute collision.
5. **Indépendance des résolveurs** :
   Chaque champ de premier niveau est résolu de façon indépendante par le moteur GraphQL, permettant une modularité et une extensibilité maximales.

---

## 🧾 Deliverables

- Complete project folder (source code + schema + tests).  
- Summary report (PDF) including:  
  - Entity and schema descriptions.  
  - Example queries and mutations.  
  - Postman test screenshots.  
  - Observations about GraphQL flexibility.

---

## 👩‍🏫 Supervision

- **Module Coordinator:** Maroua Douiri  
- **Workshop Instructors:** Badiaa Bouhdid, Akrem Khémiri, Sarra Abidi, Donia Riahi  
- **Duration:** 6 hours (Hands-on Workshop)
