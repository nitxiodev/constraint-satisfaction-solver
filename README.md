# constraint-satisfaction-solver
Implementation based on pseudocode of the book `Artificial Intelligence: A Modern Approach (3rd edition)`.

# Compatibilities
- Java: `Java 8`
- Python: `Python 2.7 or 3.5`

## Key elements
- **Sudoku**:
  - Implementation of csp algorithm based on pseudocode of the book `Artificial Intelligence: A Modern Approach (3rd edition)`.
  - Implementation of a utility to download sudoku from a variety of difficulties (`easy`, `medium`, `hard`, `evil`).
- **Map**:
  - Implementation of csp algorithm based on pseudocode of the book `Artificial Intelligence: A Modern Approach (3rd edition)`.
  - Solution of Australian map with static data (**only Java version**).
  - Implementation of a utility that loads geospatial data to paint the result on a real map, using GeoPandas and Matplotlib with GeoJSON data (**only Python version**).

## Version differences
The code written in Java is just a'port' of the python code. Both implement the same functionality except on the Java version, which doesn't make use of geospatial data to display the result on a real map.

# How to install (Java Version)
You must have `gradle` installed in order to run this version of csp solvers. Once installed, you can run one of these tasks in order to see the results:

```bash
constraint-satisfaction-solver/java/csp$ gradle run_sudoku  # run sudoku example with fixed sudoku boards.
```
```bash
constraint-satisfaction-solver/java/csp$ gradle run_sudoku_scraping  # run sudoku example with dinamic sudokus fetched from "www.websudoku.com"
```
```bash
constraint-satisfaction-solver/java/csp$ gradle run_map  # run map coloring problem for Australia map. 
```

# How to install & Run (Python Version)

```bash
constraint-satisfaction-solver/python/csp_solver$ pip install -r requirements.txt
```

## Map Configuration
The example is designed to solve the problem of colouring maps on the map of the United Kingdom. In order to change the country, you must find the following code:

```python
  input_data = {
        'country': 'united kingdom'.lower(),  # or 'ESP'....
        'key': 'admin'  # ....with 'adm0_a3'
    }
```
and change the `country` key for the desired country. The search for the country, internally, can be solved in two ways:
- Using the `key` key setted as **admin**, which is the full name of the country in **lowercase**.
- Or using the `key` key setted as **adm0_a3**, which is a [ISO 3166 Country Codes (A3)](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-3) shortcut.

## Run
```bash
constraint-satisfaction-solver/python/$ PYTHONPATH=. python csp_solver/src/sudoku/example.py # run sudoku example with fixed sudoku boards.
```
```bash
constraint-satisfaction-solver/python/$ PYTHONPATH=. python csp_solver/src/sudoku/example_scraping.py # run sudoku example with dinamic sudokus fetched from "www.websudoku.com"
```
```bash
constraint-satisfaction-solver/python/$ PYTHONPATH=. python csp_solver/src/map/example.py # run map coloring problem for UK map. 
```
