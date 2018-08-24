from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.firefox.options import Options


class Fetcher(object):
    def __init__(self):
        self._options = Options()
        self._options.headless = True
        self._driver = webdriver.Firefox(firefox_options=self._options)
        self._levels = {
            'EASY': 1,
            'MEDIUM': 2,
            'HARD': 3,
            'EVIL': 4
        }

    def __del__(self):
        self._driver.quit()

    def fetch_sudoku(self, level):
        sudoku = ''
        url = 'https://nine.websudoku.com/?level={}'.format(self._levels.get(level.upper()) or self._levels['MEDIUM'])
        self._driver.get(url)
        table = self._driver.find_element_by_id(id_='puzzle_grid')
        table_rows = table.find_elements(By.TAG_NAME, 'tr')

        for row in table_rows:
            for cell in row.find_elements(By.TAG_NAME, 'td'):
                value = cell.find_element(By.TAG_NAME, 'input').get_property('value')
                sudoku += str(value) if value else str(0)

        return sudoku
