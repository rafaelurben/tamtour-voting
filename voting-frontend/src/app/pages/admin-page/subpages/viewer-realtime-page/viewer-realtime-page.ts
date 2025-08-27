import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { ViewerRealtimeService } from '../../../../service/viewer-realtime-service';
import { AdminResultViewerKeysApi } from '../../../../api/admin/admin-result-viewer-keys.api';
import { ResultViewerKeyDto } from '../../../../dto/admin/result-viewer-key.dto';
import { Spinner } from '../../../../components/spinner/spinner';
import { Button } from '../../../../components/button/button';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

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

  protected connected = this.viewerRealtimeService.connected;
  protected connectedViewerIds = this.viewerRealtimeService.connectedViewerIds;
  protected selectedViewerIds = signal<number[]>([]);
  protected viewerKeys = signal<ResultViewerKeyDto[] | null>(null);
  protected effectiveSelectedViewerIds = computed(() =>
    this.selectedViewerIds().filter(k => this.connectedViewerIds().includes(k))
  );

  protected commandForm = this.formBuilder.group({
    action: ['', Validators.required],
    dataStr: [''],
  });

  ngOnInit() {
    this.loadViewerKeys();
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
    const data = this.commandForm.value.dataStr
      ? JSON.parse(this.commandForm.value.dataStr)
      : {};

    this.sendCommand(action, data);
  }
}
