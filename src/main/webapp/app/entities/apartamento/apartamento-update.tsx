import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IApartamento } from 'app/shared/model/apartamento.model';
import { Ala } from 'app/shared/model/enumerations/ala.model';
import { Status } from 'app/shared/model/enumerations/status.model';
import { getEntity, updateEntity, createEntity, reset } from './apartamento.reducer';

export const ApartamentoUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const apartamentoEntity = useAppSelector(state => state.apartamento.entity);
  const loading = useAppSelector(state => state.apartamento.loading);
  const updating = useAppSelector(state => state.apartamento.updating);
  const updateSuccess = useAppSelector(state => state.apartamento.updateSuccess);
  const alaValues = Object.keys(Ala);
  const statusValues = Object.keys(Status);
  const handleClose = () => {
    props.history.push('/apartamento');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...apartamentoEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ala: 'PAR',
          status: 'PRONTO',
          ...apartamentoEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hotelGovernancaApp.apartamento.home.createOrEditLabel" data-cy="ApartamentoCreateUpdateHeading">
            Create or edit a Apartamento
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="apartamento-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Numero" id="apartamento-numero" name="numero" data-cy="numero" type="text" />
              <ValidatedField label="Ala" id="apartamento-ala" name="ala" data-cy="ala" type="select">
                {alaValues.map(ala => (
                  <option value={ala} key={ala}>
                    {ala}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Responsavel" id="apartamento-responsavel" name="responsavel" data-cy="responsavel" type="text" />
              <ValidatedField label="Status" id="apartamento-status" name="status" data-cy="status" type="select">
                {statusValues.map(status => (
                  <option value={status} key={status}>
                    {status}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Detalhes" id="apartamento-detalhes" name="detalhes" data-cy="detalhes" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/apartamento" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ApartamentoUpdate;
