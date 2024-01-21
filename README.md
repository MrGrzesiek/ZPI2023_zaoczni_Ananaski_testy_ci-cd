# Zarządzanie Projektami Informatycznymi

## dr hab. inż. Radosław Wajman

## Zadanie projektowe

**Cel ćwiczenia:**
Celem ćwiczenia jest wytworzenie systemu informatycznego respektując ramy postępowania rozwoju produktu
wg metody SCRUM oraz technikę wdrażania systemu opartą na ciągłej integracji. W ramach zadań projektowych
studenci zobowiązani są do stosowania systemu kontroli wersji kodu źródłowego.

**Przygotowanie środowiska developerskiego (pipeline):**
W pierwszym kroku należy zadbać o zachowanie efektów swojej pracy z poprzedniej instrukcji. W tym celu
z gałęzi _release_ , która powinna zawierać ostateczną wersję programu, należy utworzyć gałąź _Laboratorium_1_ ,
a następnie wyczyścić gałęzie _release_ oraz _develop_. Wszystkie gałęzie _feature_ utworzone w ramach poprzedniego
ćwiczenia należy zostawić. Dodatkowo dla nowego projektu należy numerować tagi release od 2.0.0.

Kolejnym zadaniem jest skonfigurowanie systemu ciągłej integracji (CI). Grupa samodzielnie decyduje się
na jedno z licznych rozwiązań w zależności od wybranej platformy programistycznej. Należy jedynie pamiętać, że
niezależnie od wyboru systemu, należy dać pełny dostęp prowadzącemu do systemu CI. W tym celu SCRUM
Master grupy umieszcza informacje o konfiguracji systemu CI w pliku README.md w repozytorium. Tam również
powinien być odsyłacz do Backlog (np. Trello, GitHub). Jeżeli grupa korzysta z systemu CI na lokalnym serwerze,
w repozytorium powinien znaleźć się katalog CI_CFG, w którym wgrany zostanie plik konfiguracyjny
z ostatecznymi ustawieniami systemu lub raport z wykonanych ustawień.

**Wytyczne funkcjonowania oprogramowania:**
Należy opracować system informatyczny, który będzie spełniał następujące funkcjonalności:

- dane, na bazie których system będą realizowane analizy statystyczne i obliczenia, będą pochodzić
  z platformy API NBP dostępnej pod adresem [http://api.nbp.pl/,](http://api.nbp.pl/,)
- w ramach swojej funkcjonalności oprogramowanie powinno oferować następujące analizy statystyczne:
  o wyznaczenie ilości sesji wzrostowych, spadkowych i bez zmian w okresach ostatniego 1 tygodnia,
  2 tygodni, 1 miesiąca, 1 kwartału, pół roku oraz 1 roku dla wybranej przez użytkownika waluty,
  o miary statystyczne: mediana, dominanta, odchylenie standardowe i współczynnik zmienności za okres
  ostatniego 1 tygodnia, 2 tygodni, 1 miesiąca, 1 kwartału, pół roku oraz 1 roku dla wybranej przez
  użytkownika waluty,
  o rozkład zmian miesięcznych i kwartalnych w dowolnych wybranych przez użytkownika parach
  walutowych np. EUR/USD jako histogram częstości występowania zmian wartości w danym przedziale
  przedstawiony graficznie lub tekstowo. Analiza polega na wyliczeniu w kolejnych dniach sesji zmian
  w stosunku do sesji poprzednich i zsumowaniu ich w ramach liczonego okresu. Wykres lub prezentacja
  tekstowa powinny przypominać piramidkę.


**Informacje uzupełniające:**

- język implementacji systemu, platforma uruchomieniowa oraz środowisko developerskie dowolne,
- komunikacja systemu z użytkownikiem może odbywać się w dowolny sposób (tj. konsola, graficzny
  interfejs) jednakże ma udostępniać użytkownikowi możliwość sprecyzowania szczegółowych
  parametrów analizy statystycznej (np. rodzaj analizy, okres rozliczenia, rodzaj walut(y) itp.), której
  oczekuje,
- wynik przeprowadzonej analizy program może zaprezentować w dowolnej formie (np. tabeli, wykresu,
  eksportu do pliku CSV itp.),
- przypadku, gdy **grupa jest 5-cio osobowa** aplikacja musi posiadać interfejs graficzny oraz prezentować

## wynik przeprowadzonej analizy w formie wykresów.

**Uwagi do etapów projektowania i elementy wymagane do uzyskania ostatecznego zaliczenia przedmiotu**

1. Program powinien spełniać wszystkie kryteria opisane powyżej,
2. Specyfikacja wymagań przygotowana w języku angielskim zawierająca
    - założenia funkcjonalne i niefunkcjonalne,
    - diagram przypadków użycia,
    - makietę GUI, jeśli projekt realizowany jest przez grupę 5-cio osobową,
      powinna być wysłana i uzgodniona z _Product Owner_ ’em przed rozpoczęciem etapu projektowania,
3. Udział poszczególnych osób w grupie w etapach projektowania,
   Oprócz przygotowania repozytorium systemu na platformie GitHub, każdy ze studentów przygotowuje
   krótki raport zawierający informacje o przynależności do grupy projektowej, pełnionej roli oraz
   o wkładzie własnym w przygotowanie zadania projektowego w postaci listy i wysyła go w ramach
   dedykowanego zadania na platformie WIKAMP pt. „ _Instrukcja 2 - Projekt zespołowy_ ”,
4. Repozytorium projektu ma zawierać:
    - kody źródłowe
    - plik readme.md wraz z informacjami o:
      i. technologii realizacji projektu,
      ii. miejscu wdrożenia oprogramowania lub sposobie uruchamiania,
      iii. lokalizacji (folder) dokumentacji projektowej,
      iv. lokalizacji backlog’ów,
      v. sposobie realizacji CI wraz z automatyzacją testów jednostkowych,
      vi. lokalizacji raportów z procedury testowania i naprawiania oprogramowania,
5. Dokumentacja projektowa powinna zawierać:
    - listę odstępstw od specyfikacji wymagań,
    - opis architektury systemu
    - diagram komponentów,
    - diagram sekwencji,
    - diagram aktywności.
6. Product Backlog: definiowane issues, raporty z odbytych sprintów,
    - BurnDown Chart (tu uzależniony od wybranego narzędzia wspierającego projektowanie
      w SCRUM)
    - Odbycie przynajmniej dwóch sprintów (np. uzgodnienie specyfikacji wymagań i pierwsza
      wersja przy współudziale _Product Owner_ w tym jeden całkowicie w języku angielskim,
7. Utworzenie pipeline’u CI oraz implementacja testów (jednostkowych, funkcjonalnych) i ich wykonanie,
   przy czym:
   - pipeline uruchamiany jedynie dla branch’y main, release i develop,
   - testy jednostkowe mają uruchamiać się automatycznie,
   - każdy pull request wykonany na branch release przy założeniu poprawności przebiegu testów
   jednostkowych ma generować nowe otagowane wydanie (od numeru 2.0.0) oraz realizować
   wdrożenie lub dołączać wykonywalną formę projektu np. plik jar,
8. Raporty z przeprowadzonych testów (min. jeden) wraz z dokumentacją wykrycia i naprawy błędów
   (zasadniczo, co commit i build w CI to wykonanie testów i oddzielny raport) – szablon raportu
   w załączniku lub przy wykorzystaniu narzędzia np. GitHub Issue,


- Dopuszczalne jest udokumentowanie procedury w oparciu o narzędzia GitHuba np. jako issue
  typu BugFix wraz z komentarzami i procedurą naprawy błędów. W tym przypadku w pliku
  readme.md należy wyraźnie wskazać taki sposób realizacji tego zadania.
9. **Wszystkie opisy** przygotowywane dla prowadzącego (wymienione w pkt. 4 - 8) oraz wpisy w GitHub,
   Backlog, komentarze w kodzie itp. należy realizować **w języku angielskim** ,
10. Czytelny i przejrzysty kod z mile widzianymi komentarzami.

**Odwołania**

1. SCRUM
   a. https://mamopracuj.pl/jak-zostac-scrum-masterem- 6 - kluczowych-podpowiedzi
   b. https://www.qagile.pl/scrum/scrum-opis/
   c. https://kierownikprojektu.com/2017/05/31/scrum-w-pigulce-cz-1/
2. Diagramy UML
3. w kontekście statystyk kursów walut:
   a. [http://wyborcza.biz/Waluty/0,111976,,,Statystyki_waluty,USD,EBC.html](http://wyborcza.biz/Waluty/0,111976,,,Statystyki_waluty,USD,EBC.html)
   b. https://kantorlista.pl/porady-najwazniejsze-pojecia-dotyczace-wymiany-walut- 453
   c. https://analizy.investio.pl/analiza-statystyczna-zmiennosci-na-rynku-forex/
4. Continuous Integration:
   a. https://medium.com/jumperiot/how-to-build-a-continuous-integration-and-delivery-flow-
   for-embedded-software-b0b5bf220a
   b. https://medium.com/quick-code/top-tutorials-to-learn-jenkins-ci-for-testing-automation-
   93c7ac068f
   c. [http://namiekko.pl/2015/05/30/ciagla-integracja-aniol-stroz-dobrego-programisty/](http://namiekko.pl/2015/05/30/ciagla-integracja-aniol-stroz-dobrego-programisty/)
   d. https://productvision.pl/2016/continuous-integration/
5. Testowanie oprogramowania
   a. [http://testerka.pl/jak-poprawnie-zglaszac-bledy/](http://testerka.pl/jak-poprawnie-zglaszac-bledy/)
   b. https://testuj.pl/blog/jak-poprawnie-zglaszac-bledy/
   c. https://devenv.pl/jak-zglaszac-bledy/
   d. Przykładowe raporty: [http://mrbuggy.pl/mrbuggy4/docs/Raporty-z-testowania-Mr-Buggy-](http://mrbuggy.pl/mrbuggy4/docs/Raporty-z-testowania-Mr-Buggy-)
   4.pdf
