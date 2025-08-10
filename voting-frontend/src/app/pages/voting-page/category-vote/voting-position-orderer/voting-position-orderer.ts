import { Component, effect, input, output } from '@angular/core';
import { VotingCandidateDto } from '../../../../dto/voting-candidate.dto';
import { VotingPositionMapDto } from '../../../../dto/voting-position-map.dto';
import {
  CdkDrag,
  CdkDragDrop,
  CdkDragHandle,
  CdkDropList,
  moveItemInArray,
  transferArrayItem,
} from '@angular/cdk/drag-drop';
import { NgClass } from '@angular/common';
import { DragHandle } from '../../../../components/drag-handle/drag-handle';

@Component({
  selector: 'app-voting-position-orderer',
  imports: [CdkDropList, CdkDrag, NgClass, DragHandle, CdkDragHandle],
  templateUrl: './voting-position-orderer.html',
  styleUrl: './voting-position-orderer.css',
})
export class VotingPositionOrderer {
  public candidates = input.required<VotingCandidateDto[]>();
  public positionMap = input.required<VotingPositionMapDto>();
  public editable = input.required<boolean>();

  public changeMap = output<VotingPositionMapDto>();

  protected positionedCandidates: VotingCandidateDto[] = [];
  protected unpositionedCandidates: VotingCandidateDto[] = [];

  constructor() {
    effect(() => {
      const mappedCandidates = this.candidates().map(c => ({
        candidate: c,
        position: this.positionMap()[c.id],
      }));

      this.positionedCandidates = mappedCandidates
        .filter(c => c.position !== null)
        .sort((a, b) => a.position! - b.position!)
        .map(c => c.candidate);
      this.unpositionedCandidates = mappedCandidates
        .filter(c => c.position === null)
        .map(c => c.candidate)
        .sort((a, b) => a.startNumber.localeCompare(b.startNumber));
    });
  }

  private emitUpdatedPositionMap() {
    const newPositionMap: VotingPositionMapDto = {};
    this.positionedCandidates.forEach((candidate, index) => {
      newPositionMap[candidate.id] = index + 1;
    });
    this.unpositionedCandidates.forEach(candidate => {
      newPositionMap[candidate.id] = null;
    });
    this.changeMap.emit(newPositionMap);
  }

  protected drop(event: CdkDragDrop<VotingCandidateDto[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
      this.unpositionedCandidates.sort((a, b) =>
        a.startNumber.localeCompare(b.startNumber)
      );
    }
    this.emitUpdatedPositionMap();
  }
}
