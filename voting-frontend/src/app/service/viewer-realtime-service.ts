import { Injectable, OnDestroy, signal } from '@angular/core';

interface ViewerEvent {
  type: 'viewer_event';
  action: string;
  keyId: number;
}

interface ConnectedViewersEvent {
  type: 'connected_viewers';
  keyIds: number[];
}

interface ConnectedEvent {
  type: 'connected';
}

interface HeartbeatEvent {
  type: 'heartbeat';
}

type IncomingEvent =
  | ViewerEvent
  | ConnectedViewersEvent
  | ConnectedEvent
  | HeartbeatEvent;

interface CommandEvent {
  type: 'command';
  viewerIds: number[];
  action: string;
  data: object;
}

type OutgoingEvent = CommandEvent;

@Injectable({
  providedIn: 'root',
})
export class ViewerRealtimeService implements OnDestroy {
  private websocket: WebSocket | null = null;

  private readonly RECONNECT_INTERVAL = 5_000;

  private readonly _reconnect = signal(true);
  private readonly _connected = signal(false);
  private readonly _connectedViewerIds = signal<number[]>([]);

  connected = this._connected.asReadonly();
  connectedViewerIds = this._connectedViewerIds.asReadonly();

  ngOnDestroy() {
    this.disconnect();
  }

  private getUrl() {
    const proto = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const host = window.location.host;
    const path = '/api/admin/viewer/ws';
    return `${proto}//${host}${path}`;
  }

  public connect() {
    if (this.websocket) {
      console.warn('WebSocket is already created!');
      return;
    }

    this._reconnect.set(true);

    const url = this.getUrl();
    this.websocket = new WebSocket(url);
    console.log('WebSocket connection attempting to', url);

    this.websocket.onopen = () => {
      console.log('WebSocket connection established');
    };
    this.websocket.onclose = () => {
      console.log('WebSocket connection closed');
      this.websocket = null;
      this._connected.set(false);
      if (this._reconnect()) {
        setTimeout(() => this.connect(), this.RECONNECT_INTERVAL);
      }
    };
    this.websocket.onmessage = event => {
      const data: IncomingEvent = JSON.parse(event.data);
      this.handleIncomingEvent(data);
    };
    this.websocket.onerror = error => {
      console.error('WebSocket error:', error);
      this.websocket?.close();
    };
  }

  public disconnect() {
    this._reconnect.set(false);
    if (this.websocket) {
      this.websocket.close();
      this.websocket = null;
    }
    this._connected.set(false);
  }

  private handleIncomingEvent(event: IncomingEvent) {
    console.log('Incoming event:', event);
    switch (event.type) {
      case 'connected':
        this._connected.set(true);
        break;
      case 'viewer_event':
        switch (event.action) {
          case 'connected':
            this._connectedViewerIds.update(ids => [...ids, event.keyId]);
            break;
          case 'disconnected':
            this._connectedViewerIds.update(ids =>
              ids.filter(id => id !== event.keyId)
            );
            break;
          default:
            console.warn('Unknown viewer event action:', event.action);
            return;
        }
        break;
      case 'connected_viewers':
        this._connectedViewerIds.set(event.keyIds);
        break;
      case 'heartbeat':
        break;
      default:
        console.warn('Unknown event received:', event);
    }
  }

  private sendOutgoingEvent(event: OutgoingEvent) {
    if (this.websocket && this.websocket.readyState === WebSocket.OPEN) {
      this.websocket.send(JSON.stringify(event));
    } else {
      throw new Error('WebSocket is not connected!');
    }
  }

  public sendCommand(viewerIds: number[], action: string, data: object = {}) {
    const event: CommandEvent = {
      type: 'command',
      viewerIds,
      action,
      data,
    };
    this.sendOutgoingEvent(event);
  }
}
