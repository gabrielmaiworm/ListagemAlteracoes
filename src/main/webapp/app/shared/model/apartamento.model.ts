import { Ala } from 'app/shared/model/enumerations/ala.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IApartamento {
  id?: number;
  numero?: number | null;
  ala?: Ala | null;
  responsavel?: string | null;
  status?: Status | null;
  detalhes?: string | null;
}

export const defaultValue: Readonly<IApartamento> = {};
