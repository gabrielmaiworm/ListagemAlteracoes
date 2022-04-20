import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './apartamento.reducer';

export const ApartamentoDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const apartamentoEntity = useAppSelector(state => state.apartamento.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="apartamentoDetailsHeading">Apartamento</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{apartamentoEntity.id}</dd>
          <dt>
            <span id="numero">Numero</span>
          </dt>
          <dd>{apartamentoEntity.numero}</dd>
          <dt>
            <span id="ala">Ala</span>
          </dt>
          <dd>{apartamentoEntity.ala}</dd>
          <dt>
            <span id="responsavel">Responsavel</span>
          </dt>
          <dd>{apartamentoEntity.responsavel}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{apartamentoEntity.status}</dd>
          <dt>
            <span id="detalhes">Detalhes</span>
          </dt>
          <dd>{apartamentoEntity.detalhes}</dd>
        </dl>
        <Button tag={Link} to="/apartamento" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/apartamento/${apartamentoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ApartamentoDetail;
