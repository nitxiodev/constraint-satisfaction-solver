import warnings
warnings.simplefilter("ignore")
import matplotlib.pyplot as plt
from csp_solver.src.map.Map import Map
import pandas as pd
import geopandas as gp
import matplotlib, os

COLORS = {
    1: 'red',
    2: 'green',
    3: 'blue',
    4: 'yellow'
}

if __name__ == '__main__':
    geo_data = gp.read_file(os.path.join(os.path.dirname(__file__), 'data/ne_10m_admin_1_states_provinces.shp'),
                            encoding='utf8')
    geo_data['admin'] = geo_data['admin'].apply(lambda x: x.lower())
    input_data = {
        'country': 'united kingdom'.lower(),  # or 'ESP'....
        'key': 'admin'  # ....with 'adm0_a3'
    }
    s = Map(geo_data, input_data, list(COLORS.keys()), 'mrv', 'lcv')
    if s.backtracking_search():
        solution = {key: COLORS[s.variables[key]] for key in s.variables}
        df = pd.DataFrame.from_dict(solution, orient='index')
        df.reset_index(0, inplace=True)
        df.columns = ['province', 'color']

        cmap = matplotlib.colors.ListedColormap(COLORS.values())

        s_geo = s.geo_data.merge(df, left_on=s.geo_data.name, right_on='province')
        s_geo.plot(column='color', cmap=cmap)
        plt.show()

    else:
        print('No solution with these colors {}'.format(COLORS.values()))
