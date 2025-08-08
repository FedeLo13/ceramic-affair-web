import { useGoogleReCaptcha } from 'react-google-recaptcha-v3';
import './Contact.css'
import { useState } from 'react';
import { sendContactoForm } from '../../api/contactoform';

export default function Contact() {
    const { executeRecaptcha } = useGoogleReCaptcha();

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
