# E-bank

## Table of Contents
1. [Az alkalmazás specifikációja](#specification)
2. [Beadandó feladat](#task)

<a name="specification"></a>
## Az alkalmazás specifikációja
Olyan internetes alkalmazás, amely lehetővé teszi az ügyfelek számára, a banki számlájaik kezelését.  
**A létrehozandó *domain* osztályok:**

![image](./documentation/bank-model.jpg)

### Az UML osztálydiagramon a következők láthatók:

- Az osztályok, mezőik legtöbbjével

Bank mezői/kapcsolatai:

- accounts: A bank számlái
- users: A bank felhasználói
- transactions: A bank tranzakciói

User mezői/kapcsolatai:

- id: a felhasználó azonosítója
- name: A felhasználó neve (például „Kiss Pista”)
- password: A felhasználó jelszava
- A User osztályból származnak a Customer és Admin osztályok

Customer mezői/kapcsolatai:

- phoneNum: Az ügyfél telefonszáma
- address: Az ügyfél lakcíme
- dateOfBirth: Az ügyfél születési dátuma
- ownAccounts: A felhasználó számláinak listája

Admin mezői/kapcsolatai:

- customers: A bank ügyfelei

Account osztály tartalma:

- balance: A számlának az egyenlegét tárolja
- accountNumber: A számlának az azonosító száma
- currency: A számla pénzneme
- ownTransactions: A számlához tartozó tranzakciók listája

Transaction osztály:

- amount: A tranzakció mennyisége
- comment: Esetleges hozzászólás
- date: A tranzakció végrehajtásának dátuma
- recipient: A tranzakciót fogadó számla
- isWithdrawal: Mutatja, hogy utalásról vagy pénz betételről, kivételről van-e szó

Status enum osztály:

- A tranzakció állapotát jelzi
- UNSIGNED: A tranzakció még nincs jóváhagyva
- INPROGRESS: A tranzakció folyamatban van
- COMPLETED : A tranzakció sikeresen végrehajtódott
- FAILED: A tranzakció végrehajtása sikertelen lett

<a name="task"></a>
## Beadandó feladat

- Bankon keresztüli átutalás
- Számlatörténet lekérdezése (utalások, pénzbetétel, pénzkivétel)
- Számla és felhasználó részletes adatainak lekérdezése
- Adminisztrátor által való módosítása a felhasználók adatainak
- Bejelentkezés, regisztráció