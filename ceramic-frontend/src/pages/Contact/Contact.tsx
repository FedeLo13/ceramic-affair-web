import { useGoogleReCaptcha } from 'react-google-recaptcha-v3';
import './Contact.css'
import { useState, useEffect } from 'react';
import { sendContactoForm } from '../../api/contactoform';
import { subscribe } from '../../api/suscriptores';
import { ValidationError } from '../../api/utils';
import { useLocation } from 'react-router-dom';

export default function Contact() {
    const { executeRecaptcha } = useGoogleReCaptcha();

    // Estados para la newsletter
    const [newsletterEmail, setNewsletterEmail] = useState('');
    const [newsletterStatus, setNewsletterStatus] = useState<string | null>(null);
    const [newsletterError, setNewsletterError] = useState<string | null>(null);
    const [newsletterLoading, setNewsletterLoading] = useState(false);

    // Estados para toasts
    const [toastMessage, setToastMessage] = useState<string | null>(null);
    const [visibleToast, setVisibleToast] = useState(false);

    const location = useLocation();

    // Scroll a la sección de newsletter si el hash está presente en la URL
    useEffect(() => {
        if (location.hash === '#newsletter') {
            const newsletterSection = document.getElementById('newsletter');
            if (newsletterSection) {
                setTimeout(() => {
                    newsletterSection.scrollIntoView({ behavior: 'smooth' });
                }, 200);
            }
        }
    }, [location]);

    // Lógica para mostrar y ocultar toasts
    useEffect(() => {
        if (toastMessage) {
            setVisibleToast(true);
            const timer = setTimeout(() => setVisibleToast(false), 3000);
            return () => clearTimeout(timer);
        }
    }, [toastMessage]);

    const handleToastTransitionEnd = () => {
        if (!visibleToast) setToastMessage(null);
    };

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

            setToastMessage('Message sent successfully!');
            setForm({
                nombre: '',
                apellidos: '',
                email: '',
                asunto: '',
                mensaje: ''
            });

        } catch (error) {
            console.error('Error sending message:', error);
            setToastMessage('Error sending message. Please try again later.');
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
            {/* Formulario de contacto */}
            <div className='contact-text-block'>
                <p className='contact-text'>If you have any questions about the process, please don’t hesitate to get in touch. I’ll get back to you as soon as possible to answer your inquiries.</p>
                <p className='contact-text'>You can also subscribe to my newsletter below the form to receive updates and exclusive content.</p>
                <p className='contact-text'>Si tienes alguna pregunta sobre el proceso, no dudes en ponerte en contacto conmigo. Te responderé lo antes posible para resolver tus dudas.</p>
                <p className='contact-text'>También puedes suscribirte a mi newsletter debajo del formulario para recibir actualizaciones y contenido exclusivo.</p>
            </div>
            <section className="contact-form-section">
                <h2 className='contact-title'>Send a Message</h2>
                <form className="contact-form" onSubmit={handleSubmit}>
                    <div className="form-group name-fields">
                        <div className="name-field">
                            <label htmlFor="nombre" className='contact-label'>First Name</label>
                            <input type="text" id="nombre" name="nombre" className='contact-input' value={form.nombre} onChange={handleChange} required />
                        </div>
                        <div className="name-field">
                            <label htmlFor="apellidos" className='contact-label'>Last Name</label>
                            <input type="text" id="apellidos" name="apellidos" className='contact-input' value={form.apellidos} onChange={handleChange} required />
                        </div>
                    </div>

                    <div className="form-group">
                        <label htmlFor="email" className='contact-label'>Email</label>
                        <input type="email" id="email" name="email" className='contact-input' value={form.email} onChange={handleChange} required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="asunto" className='contact-label'>Subject</label>
                        <input type="text" id="asunto" name="asunto" className='contact-input' value={form.asunto} onChange={handleChange} required />
                    </div>

                    <div className="form-group">
                        <label htmlFor="mensaje" className='contact-label'>Message</label>
                        <textarea id="mensaje" name="mensaje" className='contact-message-input' rows={5} value={form.mensaje} onChange={handleChange} required></textarea>
                    </div>

                    <button type="submit" className="contact-button">Submit</button>
                </form>
            </section>

            <div className='contact-text-block'>
                <p className='contact-text'>Enter your email down below to subscribe to my newsletter and receive emails about new collections, events, and exclusive content.</p>
                <p className='contact-text'>Introduce tu correo electrónico abajo para suscribirte a mi newsletter y recibir emails sobre nuevas colecciones, eventos y contenido exclusivo.</p>
            </div>

            {/* Newsletter */}
            <section id="newsletter" className="newsletter-section">
                <h2 className='newsletter-title'>Newsletter</h2>
                <form className="newsletter-form" onSubmit={handleNewsletterSubmit}>
                    <div className="form-group">
                        <input 
                            type="email" 
                            id="newsletterEmail" 
                            name="newsletterEmail"
                            placeholder='Email Adress'
                            className='contact-input'
                            value={newsletterEmail} 
                            onChange={handleNewsletterChange} 
                            required
                            disabled={newsletterLoading}
                        />
                    </div>
                    <button type="submit" className="contact-button" disabled={newsletterLoading}>
                        {newsletterLoading ? 'Loading...' : 'Subscribe'}
                    </button>
                </form>

                {newsletterStatus && <p className="success-message">{newsletterStatus}</p>}
                {newsletterError && <p className="error-message">{newsletterError}</p>}
            </section>

            {toastMessage && (
                <div
                    className={`toast-message ${visibleToast ? 'show' : 'hide'}`}
                    onTransitionEnd={handleToastTransitionEnd}
                >
                    {toastMessage}
                    <button
                        className="toast-close-btn"
                    onClick={() => setVisibleToast(false)}
                    >
                    ×
                    </button>
                </div>
            )}
        </div>
    );
}
