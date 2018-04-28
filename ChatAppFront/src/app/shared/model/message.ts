export class Message {
    private messageType: string;
    private content: string;

    constructor(mt, ct: string) {
        this.messageType = mt;
        this.content = ct;
    }
}
