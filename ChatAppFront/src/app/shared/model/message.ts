export class Message {
    public messageType: string;
    public content: any;
    public loggedUserName : string;

    constructor(mt, ct, lu: string) {
        this.messageType = mt;
        this.content = ct;
        this.loggedUserName = lu;
    }
}
