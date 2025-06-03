import './Contact.css'

export default function Contact() {
    return (
        <div className="contact-page">
            <h1>Contact</h1>

            {/* Formulario de contacto */}
            <section className="contact-form-section">
                <h2>Send a Message</h2>
                <form className="contact-form">
                    <div className="form-group">
                        <label htmlFor="nombre">First Name</label>
                        <input type="text" id="nombre" name="nombre" required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="apellidos">Last Name</label>
                        <input type="text" id="apellidos" name="apellidos" required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="email">Email</label>
                        <input type="email" id="email" name="email" required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="asunto">Subject</label>
                        <input type="text" id="asunto" name="asunto" required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="mensaje">Message</label>
                        <textarea id="mensaje" name="mensaje" rows={5} required></textarea>
                    </div>

                    <button type="submit" className="submit-button">Send</button>
                </form>
            </section>

            {/* Newsletter */}
            <section className="newsletter-section">
                <h2>Subscribe to our Newsletter</h2>
                <form className="newsletter-form">
                    <div className="form-group">
                        <label htmlFor="newsletterEmail">Email</label>
                        <input type="email" id="newsletterEmail" name="newsletterEmail" required />
                    </div>
                    <button type="submit" className="subscribe-button">Subscribe</button>
                </form>
            </section>
        </div>
    );
}
