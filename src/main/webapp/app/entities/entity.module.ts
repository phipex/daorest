import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { PdanyosClienteModule } from './cliente/cliente.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        PdanyosClienteModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PdanyosEntityModule {}
