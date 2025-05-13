export default class SystemStatus{
    name: SystemStatusName | undefined;
    value: SystemStatusNameValue | undefined;
}

export enum SystemStatusName {
    JOB_INDEXER = 'JOB_INDEXER',
}

export enum SystemStatusNameValue {
    JOB_INDEXING = 'JOB_INDEXING',
    JOB_INDEXER_IDLE = 'JOB_INDEXER_IDLE',
    JOB_INDEXER_ERROR = 'JOB_INDEXER_ERROR',
}