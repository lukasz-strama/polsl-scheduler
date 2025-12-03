# POLSL Personal Scheduler

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Build Status](https://github.com/lukasz-strama/polsl-scheduler/actions/workflows/javadoc.yml/badge.svg)
[![Javadoc](https://img.shields.io/badge/Javadoc-Link-blue)](https://lukasz-strama.github.io/polsl-scheduler/)

Aplikacja desktopowa do zarządzania planem zajęć na Politechnice Śląskiej.

## Jak uruchomić

1.  Sklonuj repozytorium:
    ```bash
    git clone https://github.com/lukasz-strama/polsl-scheduler.git
    ```
2.  Wejdź do katalogu projektu:
    ```bash
    cd polsl-scheduler
    ```
3.  Uruchom aplikację przez Maven:
    ```bash
    mvn clean javafx:run
    ```

> **Uwaga:** Przy pierwszym uruchomieniu aplikacja automatycznie utworzy plik `scheduler.db` i wypełni go danymi testowymi.

## Dokumentacja

Pełna dokumentacja techniczna (JavaDoc) generowana automatycznie przez GitHub Actions jest dostępna tutaj:
**[Zobacz Dokumentację API](https://lukasz-strama.github.io/polsl-scheduler/)**