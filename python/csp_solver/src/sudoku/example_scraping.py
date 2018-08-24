from csp_solver.src.sudoku.Fetcher import Fetcher
from csp_solver.src.sudoku.Sudoku import Sudoku

if __name__ == '__main__':
    # We get one sudoku per class
    sudokus = {
        'easy': '',
        'medium': '',
        'hard': '',
        'evil': ''
    }

    f = Fetcher()
    for sudoku in sudokus:
        sudokus[sudoku] = f.fetch_sudoku(sudoku.upper())

    # Solve sudokus
    for sudoku in sudokus:
        s = Sudoku(sudokus[sudoku], 'mrv', 'lcv')
        print('RAW SUDOKU [%s]: %s\n' % (sudoku.upper(), sudokus[sudoku]))
        if s.backtracking_search():
            print(s.pretty_print())
        else:
            print('No solution :(')
