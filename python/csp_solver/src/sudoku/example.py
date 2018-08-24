from datetime import datetime

from csp_solver.src.sudoku.Sudoku import Sudoku


def run_backtracking(sudoku, sudokus):
    before = datetime.now()
    s = Sudoku(sudokus[sudoku], 'mrv', 'lcv')
    if s.backtracking_search():
        print(s.pretty_print())
    else:
        print('No solution :(')

    now = datetime.now() - before
    print('RAW SUDOKU [%s]: %s (%.3fs)\n' % (sudoku.capitalize(), sudokus[sudoku], now.total_seconds()))


if __name__ == '__main__':
    sudokus = {
        'easy': '000030407570980360040706001000005043050803070380200000800401020012079038905020000',
        'medium': '000050000004700080200048030045000328900000001826000940060930002080002600000060000',
        'hard': '004009270000001003000000910080420050090070080060058030015000000900800000038700400',
        'evil': '003500400000400050000060109020009004908000703100700020301080000080001000006002300'
    }

    for sudoku in sudokus:
        run_backtracking(sudoku, sudokus)
