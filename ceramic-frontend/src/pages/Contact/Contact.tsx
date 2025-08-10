import { useGoogleReCaptcha } from 'react-google-recaptcha-v3';
import './Contact.css'
import { useState } from 'react';
import { sendContactoForm } from '../../api/contactoform';
import { subscribe } from '../../api/suscriptores';
import { ValidationError } from '../../api/utils';

export default function Contact() {
    const { executeRecaptcha } = useGoogleReCaptcha();

    // Estados para la newsletter
    const [newsletterEmail, setNewsletterEmail] = useState('');
    const [newsletterStatus, setNewsletterStatus] = useState<string | null>(null);
    const [newsletterError, setNewsletterError] = useState<string | null>(null);
    const [newsletterLoading, setNewsletterLoading] = useState(false);

    // Lógica del formulario de contacto
    const [form, setForm] = useState({
        nombre: '',
        apellidos: '',
        email: '',
        asunto: '',
        mensaje: ''
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setForm({...form, [e.target.name]: e.target.value});
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if(!executeRecaptcha) {
            console.error('Recaptcha not executed');
            return;
        }

        try {
            const token = await executeRecaptcha('contactForm');

            await sendContactoForm({
            ...form,
            recaptchaToken: token
            });

            alert('Message sent successfully!');
            setForm({
                nombre: '',
                apellidos: '',
                email: '',
                asunto: '',
                mensaje: ''
            });

        } catch (error) {
            console.error('Error sending message:', error);
            alert('Error sending message. Please try again later.');
        }
    }

    // Lógica de la newsletter
    const handleNewsletterChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setNewsletterEmail(e.target.value);
        setNewsletterStatus(null);
        setNewsletterError(null);
    }

    const handleNewsletterSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setNewsletterLoading(true);
        setNewsletterStatus(null);
        setNewsletterError(null);

        if (!executeRecaptcha) {
            setNewsletterError('Recaptcha not executed');
            setNewsletterLoading(false);
            return;
        }

        try {
            const token = await executeRecaptcha('newsletterSubscribe');

            const message = await subscribe({
                email: newsletterEmail,
                recaptchaToken: token
            });

            setNewsletterStatus(message);
            setNewsletterEmail('');
        } catch (error) {
            if (error instanceof ValidationError) {
                setNewsletterError('Validation error: ' + error.message);
            } else if (error instanceof Error) {
                setNewsletterError(error.message);
            } else {
                setNewsletterError('Unknown error');
            }
        } finally {
            setNewsletterLoading(false);
        }
    }

    return (
        <div className="contact-page">
            <h1>Contact</h1>

            {/* Formulario de contacto */}
            <section className="contact-form-section">
                <h2>Send a Message</h2>
                <form className="contact-form" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="nombre">First Name</label>
                        <input type="text" id="nombre" name="nombre" value={form.nombre} onChange={handleChange} required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="apellidos">Last Name</label>
                        <input type="text" id="apellidos" name="apellidos" value={form.apellidos} onChange={handleChange} required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="email">Email</label>
                        <input type="email" id="email" name="email" value={form.email} onChange={handleChange} required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="asunto">Subject</label>
                        <input type="text" id="asunto" name="asunto" value={form.asunto} onChange={handleChange} required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="mensaje">Message</label>
                        <textarea id="mensaje" name="mensaje" rows={5} value={form.mensaje} onChange={handleChange} required></textarea>
                    </div>

                    <button type="submit" className="submit-button">Send</button>
                </form>
            </section>

            {/* Newsletter */}
            <section className="newsletter-section">
                <h2>Subscribe to our Newsletter</h2>
                <form className="newsletter-form" onSubmit={handleNewsletterSubmit}>
                    <div className="form-group">
                        <label htmlFor="newsletterEmail">Email</label>
                        <input 
                            type="email" 
                            id="newsletterEmail" 
                            name="newsletterEmail" 
                            value={newsletterEmail} 
                            onChange={handleNewsletterChange} 
                            required
                            disabled={newsletterLoading}
                        />
                    </div>
                    <button type="submit" className="subscribe-button" disabled={newsletterLoading}>
                        {newsletterLoading ? 'Loading...' : 'Subscribe'}
                    </button>
                </form>

                {newsletterStatus && <p className="success-message">{newsletterStatus}</p>}
                {newsletterError && <p className="error-message">{newsletterError}</p>}
            </section>
        </div>
    );
}
