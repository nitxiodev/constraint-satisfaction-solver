import unittest

from hamcrest import assert_that, equal_to, contains, is_

from csp_solver.src.csp.AC3 import AC3
from csp_solver.src.csp.CSP import CSP, UNASSIGNED
from csp_solver.src.sudoku.Sudoku import Sudoku


class DummyCSP(CSP):
    def __init__(self, domains, variable_heur=None, value_heur=None):
        self._domains = {'v1': domains[0], 'v2': domains[1]}
        self._variables = {key: UNASSIGNED for key in self._domains}
        self._constraints = lambda x, y: x != y
        super(DummyCSP, self).__init__(self._variables, self._domains, self._constraints, variable_heur, value_heur)
        self.build_neighbors()

    @property
    def domains(self):
        return self._domains

    def build_neighbors(self):
        self._neighbors['v1'] = {'v2'}
        self._neighbors['v2'] = {'v1'}

    def solved(self):
        return all([self.variables[v] != UNASSIGNED for v in self.variables])

    def valid_assignment(self, variable, value):
        variable_neighbors = self.neighbors[variable]
        return all([self.variables[neighbor] != value for neighbor in variable_neighbors])


class AC3Tests(unittest.TestCase):
    def setUp(self):
        self._ac3 = AC3()
        self._csp = DummyCSP([[1, 2, 3], [3]])

    def tearDown(self):
        pass

    def testAlgorithmState(self):
        self._ac3.ac3(self._csp)
        assert_that(self._csp.domains, equal_to({'v1': [1, 2], 'v2': [3]}))


class CSPTests(unittest.TestCase):
    def setUp(self):
        self._csp = DummyCSP([[1, 2, 3], [3]])
        self._csp_ns = DummyCSP([[3], [3]])
        self._csp2 = DummyCSP([[1, 2, 3], [3]], variable_heur='mrv', value_heur='lcv')
        self._csp_consistent = DummyCSP([[1], [3]])

    def testSolution(self):
        self._csp.backtracking_search()
        assert_that(self._csp.variables.values(), not contains(UNASSIGNED, UNASSIGNED))

    def testSolution2(self):
        self._csp2.backtracking_search()
        assert_that(self._csp2.variables.values(), not contains(UNASSIGNED, UNASSIGNED))

    def testSolutionConsistent(self):
        self._csp_consistent.backtracking_search()
        assert_that(self._csp_consistent.variables, equal_to({'v1': 1, 'v2': 3}))

    def testNoSolution(self):
        self._csp_ns.backtracking_search()
        assert_that(self._csp_ns.variables, equal_to({'v1': UNASSIGNED, 'v2': UNASSIGNED}))


class SudokuTests(unittest.TestCase):
    def setUp(self):
        self._sudoku = '000030407570980360040706001000005043050803070380200000800401020012079038905020000'
        self._solver = Sudoku(self._sudoku, 'mrv', 'lcv')

    def testSolved(self):
        result = self._solver.solved()
        assert_that(result, is_(False))

    def testValidAssignment(self):
        result = self._solver.valid_assignment('A_3', 3)
        assert_that(result, is_(True))

    def testBuildNeighbors(self):
        neighbors = [len(self._solver.neighbors[x]) for x in self._solver.neighbors]
        assert_that(neighbors, equal_to([20] * 81))
