import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'formatDuration',
    standalone: true
})
export class FormatDurationPipe implements PipeTransform {
    transform(value: number | undefined | null): string {
        if (!value) {
            return '0:00';
        }

        const totalSeconds = Math.floor(value / 1000);
        const minutes: number = Math.floor(totalSeconds / 60);
        const seconds: number = totalSeconds % 60;

        return `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
    }
}