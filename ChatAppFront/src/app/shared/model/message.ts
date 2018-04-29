export class Message {
    private messageType: string;
    private content: string;
    private loggedUserName : string;

    constructor(mt, ct, lu: string) {
        this.messageType = mt;
        this.content = ct;
        this.loggedUserName = lu;
    }
}
