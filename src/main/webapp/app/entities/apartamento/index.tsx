import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Apartamento from './apartamento';
import ApartamentoDetail from './apartamento-detail';
import ApartamentoUpdate from './apartamento-update';
import ApartamentoDeleteDialog from './apartamento-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ApartamentoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ApartamentoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ApartamentoDetail} />
      <ErrorBoundaryRoute path={match.url} component={Apartamento} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ApartamentoDeleteDialog} />
  </>
);

export default Routes;
