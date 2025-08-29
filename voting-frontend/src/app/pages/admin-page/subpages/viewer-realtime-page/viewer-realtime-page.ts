import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { ViewerRealtimeService } from '../../../../service/viewer-realtime-service';
import { AdminResultViewerKeysApi } from '../../../../api/admin/admin-result-viewer-keys.api';
import { ResultViewerKeyDto } from '../../../../dto/admin/result-viewer-key.dto';
import { Spinner } from '../../../../components/spinner/spinner';
import { Button } from '../../../../components/button/button';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AdminVotingCategoriesApi } from '../../../../api/admin/admin-voting-categories.api';
import { VotingCategoryBaseDto } from '../../../../dto/voting-category-base.dto';

@Component({
  selector: 'app-viewer-realtime-page',
  imports: [Spinner, Button, ReactiveFormsModule],
  templateUrl: './viewer-realtime-page.html',
  styleUrl: './viewer-realtime-page.css',
})
export class ViewerRealtimePage implements OnInit {
  private readonly resultViewerKeysApi = inject(AdminResultViewerKeysApi);
  private readonly formBuilder = inject(FormBuilder);
  private readonly viewerRealtimeService = inject(ViewerRealtimeService);
  private readonly categoryApi = inject(AdminVotingCategoriesApi);

  protected connected = this.viewerRealtimeService.connected;
  protected connectedViewerIds = this.viewerRealtimeService.connectedViewerIds;
  protected selectedViewerIds = signal<number[]>([]);
  protected viewerKeys = signal<ResultViewerKeyDto[] | null>(null);
  protected categories = signal<VotingCategoryBaseDto[]>([]);
  protected effectiveSelectedViewerIds = computed(() =>
    this.selectedViewerIds().filter(k => this.connectedViewerIds().includes(k))
  );

  protected resultsLoading = signal(false);
  protected resultsLoadingId = signal<number | null>(null);

  protected commandForm = this.formBuilder.group({
    action: ['', Validators.required],
    dataStr: [''],
  });

  ngOnInit() {
    this.loadViewerKeys();
    this.loadCategories();
    this.viewerRealtimeService.connect();
  }

  private loadViewerKeys() {
    this.resultViewerKeysApi.getKeys().subscribe({
      next: keys => this.viewerKeys.set(keys),
      error: err => {
        console.error('Failed to load viewer keys', err);
      },
    });
  }

  private loadCategories() {
    this.categoryApi.getCategories().subscribe({
      next: cats => this.categories.set(cats),
      error: err => {
        console.error('Failed to load categories', err);
      },
    });
  }

  protected loadResults(categoryId: number) {
    this.resultsLoading.set(true);
    this.resultsLoadingId.set(categoryId);
    this.categoryApi.getCategoryResult(categoryId).subscribe({
      next: result => {
        this.resultsLoading.set(false);
        this.commandForm.setValue({
          action: 'load_result_data',
          dataStr: JSON.stringify(result),
        });
      },
      error: () => {
        this.resultsLoading.set(false);
        alert('Resultate konnten nicht geladen werden!');
      },
    });
  }

  protected toggleViewerSelection(viewerId: number) {
    this.selectedViewerIds.update(ids =>
      ids.includes(viewerId)
        ? ids.filter(id => id !== viewerId)
        : [...ids, viewerId]
    );
  }

  protected sendCommand(action: string, data: object = {}) {
    this.viewerRealtimeService.sendCommand(
      this.effectiveSelectedViewerIds(),
      action,
      data
    );
  }

  protected submitForm() {
    if (!this.commandForm.valid) {
      return;
    }

    const action = this.commandForm.value.action!;
    let data = {};
    if (this.commandForm.value.dataStr) {
      try {
        data = JSON.parse(this.commandForm.value.dataStr);
      } catch (e) {
        alert('Ungültige JSON-Daten');
        console.error('Failed to parse JSON data', e);
        return;
      }
    }

    this.sendCommand(action, data);
  }
}
