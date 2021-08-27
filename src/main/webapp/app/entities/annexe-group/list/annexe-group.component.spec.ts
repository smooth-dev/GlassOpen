import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AnnexeGroupService } from '../service/annexe-group.service';

import { AnnexeGroupComponent } from './annexe-group.component';

describe('Component Tests', () => {
  describe('AnnexeGroup Management Component', () => {
    let comp: AnnexeGroupComponent;
    let fixture: ComponentFixture<AnnexeGroupComponent>;
    let service: AnnexeGroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AnnexeGroupComponent],
      })
        .overrideTemplate(AnnexeGroupComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AnnexeGroupComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AnnexeGroupService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 'ABC' }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.annexeGroups?.[0]).toEqual(jasmine.objectContaining({ id: 'ABC' }));
    });
  });
});
