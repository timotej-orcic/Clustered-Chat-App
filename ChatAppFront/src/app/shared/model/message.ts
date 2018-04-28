export class Message {
    private MessageType: string;
    private Content: string;

    constructor(mt, ct: string) {
        this.MessageType = mt;
        this.Content = ct;
    }
}
