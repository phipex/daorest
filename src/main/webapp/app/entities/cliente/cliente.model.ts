import { BaseEntity } from './../../shared';

export class Cliente implements BaseEntity {
    constructor(
        public id?: number,
        public idCliente?: string,
        public nombre?: string,
        public apellido?: string,
        public segundoNombre?: string,
        public segundoApellido?: string,
    ) {
    }
}
